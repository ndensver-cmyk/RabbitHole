#!/usr/bin/env python3
"""Fetch locally bundled editorial poster images from Wikimedia Commons.

The app keeps these images inside the APK so poster artwork is visible even when
runtime image APIs are slow or unavailable. Wikimedia Commons file pages and
authors are recorded in credits.txt.
"""
import json
import os
import re
import sys
import time
import urllib.parse
import urllib.request

OUT = "app/src/main/assets/posters"
UA = "RabbitHole/5.5.0 (https://github.com/ndensver-cmyk/RabbitHole)"
POSTERS = {
    "poster_nirvana": "Nirvana band",
    "poster_nevermind": "Nirvana band concert",
    "poster_inutero": "Nirvana band 1993",
    "poster_seattle": "Seattle skyline",
    "poster_grunge": "grunge rock concert",
    "poster_pink_floyd": "Pink Floyd band",
    "poster_dark_side": "Pink Floyd prism Dark Side of the Moon",
    "poster_daftpunk": "Daft Punk duo",
    "poster_discovery": "Daft Punk live electronic music",
    "poster_interstella": "Interstella 5555 anime",
    "poster_cinema": "cinema film camera",
    "poster_apollo11": "Apollo 11 moon landing",
    "poster_voyager": "Voyager spacecraft NASA",
    "poster_design_history": "graphic design history vintage posters",
}

def request_json(url):
    req = urllib.request.Request(url, headers={"User-Agent": UA, "Accept": "application/json"})
    with urllib.request.urlopen(req, timeout=25) as response:
        return json.loads(response.read().decode("utf-8"))

def search_commons(query):
    params = {
        "action": "query", "generator": "search", "gsrsearch": query,
        "gsrnamespace": "6", "gsrlimit": "15", "prop": "imageinfo",
        "iiprop": "url|extmetadata", "iiurlwidth": "1500", "format": "json"
    }
    url = "https://commons.wikimedia.org/w/api.php?" + urllib.parse.urlencode(params)
    data = request_json(url)
    pages = data.get("query", {}).get("pages", {})
    for page in pages.values():
        title = page.get("title", "")
        info = (page.get("imageinfo") or [{}])[0]
        thumb = info.get("thumburl") or info.get("url") or ""
        if not thumb.startswith("https://"):
            continue
        if re.search(r"\.(svg|gif|tif|tiff)(\?|$)", thumb, re.I):
            continue
        if not re.search(r"\.(jpg|jpeg|png|webp)(\?|$)", thumb, re.I):
            continue
        return thumb, title, info.get("descriptionurl", ""), info.get("extmetadata", {})
    return None

def download(url, path):
    req = urllib.request.Request(url, headers={"User-Agent": UA, "Referer": "https://commons.wikimedia.org/"})
    with urllib.request.urlopen(req, timeout=35) as response:
        content_type = response.headers.get("Content-Type", "")
        data = response.read()
    if not content_type.startswith("image/") or len(data) < 5000:
        raise ValueError("Not a usable image (%s, %s bytes)" % (content_type, len(data)))
    with open(path, "wb") as output:
        output.write(data)

def clean(value):
    return re.sub(r"<[^>]+>", "", str(value or "")).strip()

def main():
    os.makedirs(OUT, exist_ok=True)
    credits = []
    success = 0
    for filename, query in POSTERS.items():
        path = os.path.join(OUT, filename + ".jpg")
        try:
            found = search_commons(query)
            if not found:
                print("WARNING: no Commons image found for", filename)
                continue
            url, title, page_url, metadata = found
            download(url, path)
            artist = clean(metadata.get("Artist", {}).get("value", ""))
            license_name = clean(metadata.get("LicenseShortName", {}).get("value", ""))
            credits.append("%s | %s | %s | %s | %s" % (filename, title, artist, license_name, page_url))
            success += 1
            print("Downloaded %s (%d bytes): %s" % (filename, os.path.getsize(path), title))
            time.sleep(0.25)
        except Exception as error:
            print("WARNING: poster %s failed: %s" % (filename, error))
    with open(os.path.join(OUT, "credits.txt"), "w", encoding="utf-8") as output:
        output.write("Wikimedia Commons image credits for RabbitHole\n\n")
        output.write("\n".join(credits))
        output.write("\n")
    print("POSTER_ASSETS_DOWNLOADED=%d/%d" % (success, len(POSTERS)))
    if success < 5:
        print("ERROR: too few offline poster images were fetched; refusing to ship a text-only visual build.")
        sys.exit(1)

if __name__ == "__main__":
    main()
