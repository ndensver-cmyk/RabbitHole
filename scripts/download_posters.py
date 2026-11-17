#!/usr/bin/env python3
"""Fetch offline editorial poster images from Wikimedia Commons."""
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
    "poster_nirvana": ["Kurt Cobain Nirvana band", "Nirvana band"],
    "poster_nevermind": ["Nirvana band Kurt Cobain", "Nirvana band live"],
    "poster_inutero": ["Nirvana band 1993", "Kurt Cobain Nirvana"],
    "poster_seattle": ["Seattle skyline city"],
    "poster_grunge": ["Nirvana Soundgarden Pearl Jam grunge", "grunge music Seattle bands"],
    "poster_pink_floyd": ["Pink Floyd band members", "Pink Floyd live concert"],
    "poster_dark_side": ["Pink Floyd Dark Side of the Moon prism", "Pink Floyd prism"],
    "poster_daftpunk": ["Daft Punk musicians helmets", "Daft Punk live"],
    "poster_discovery": ["Daft Punk Discovery era", "Daft Punk live electronic music"],
    "poster_interstella": ["Interstella 5555 anime film", "Leiji Matsumoto anime"],
    "poster_cinema": ["cinema film camera movie camera"],
    "poster_apollo11": ["Apollo 11 moon landing astronauts"],
    "poster_voyager": ["Voyager 1 spacecraft NASA", "Voyager spacecraft NASA"],
    "poster_design_history": ["graphic design history poster", "Bauhaus graphic design poster"],
}

def request_json(url):
    req = urllib.request.Request(url, headers={"User-Agent": UA, "Accept": "application/json"})
    with urllib.request.urlopen(req, timeout=25) as response:
        return json.loads(response.read().decode("utf-8"))

def search_commons(query):
    params = {
        "action": "query", "generator": "search", "gsrsearch": query,
        "gsrnamespace": "6", "gsrlimit": "25", "prop": "imageinfo",
        "iiprop": "url|extmetadata|mime", "iiurlwidth": "1500", "format": "json"
    }
    url = "https://commons.wikimedia.org/w/api.php?" + urllib.parse.urlencode(params)
    data = request_json(url)
    pages = data.get("query", {}).get("pages", {})
    candidates = []
    for page in pages.values():
        title = page.get("title", "")
        if re.search(r"\.(pdf|djvu|svg|gif|tif|tiff)$", title, re.I):
            continue
        info = (page.get("imageinfo") or [{}])[0]
        thumb = info.get("thumburl") or info.get("url") or ""
        mime = info.get("mime", "")
        if not thumb.startswith("https://") or not mime.startswith("image/"):
            continue
        if re.search(r"\.(svg|gif|tif|tiff)(\?|$)", thumb, re.I):
            continue
        candidates.append((thumb, title, info.get("descriptionurl", ""), info.get("extmetadata", {})))
    return candidates

def download(url, path):
    req = urllib.request.Request(url, headers={"User-Agent": UA, "Referer": "https://commons.wikimedia.org/"})
    with urllib.request.urlopen(req, timeout=35) as response:
        content_type = response.headers.get("Content-Type", "")
        data = response.read()
    if not content_type.startswith("image/") or len(data) < 12000:
        raise ValueError("Not a usable image (%s, %s bytes)" % (content_type, len(data)))
    with open(path, "wb") as output:
        output.write(data)

def clean(value):
    return re.sub(r"<[^>]+>", "", str(value or "")).strip()

def main():
    os.makedirs(OUT, exist_ok=True)
    credits = []
    success = 0
    for filename, queries in POSTERS.items():
        path = os.path.join(OUT, filename + ".jpg")
        found_image = False
        for query in queries:
            try:
                candidates = search_commons(query)
                for url, title, page_url, metadata in candidates:
                    try:
                        download(url, path)
                        artist = clean(metadata.get("Artist", {}).get("value", ""))
                        license_name = clean(metadata.get("LicenseShortName", {}).get("value", ""))
                        credits.append("%s | %s | %s | %s | %s" % (filename, title, artist, license_name, page_url))
                        success += 1
                        found_image = True
                        print("Downloaded %s (%d bytes): %s" % (filename, os.path.getsize(path), title))
                        break
                    except Exception:
                        if os.path.exists(path):
                            os.remove(path)
                if found_image:
                    break
            except Exception as error:
                print("WARNING: search failed for %s (%s): %s" % (filename, query, error))
            time.sleep(0.2)
        if not found_image:
            print("WARNING: no suitable Commons image found for", filename)
    with open(os.path.join(OUT, "credits.txt"), "w", encoding="utf-8") as output:
        output.write("Wikimedia Commons image credits for RabbitHole\n\n")
        output.write("\n".join(credits))
        output.write("\n")
    print("POSTER_ASSETS_DOWNLOADED=%d/%d" % (success, len(POSTERS)))
    if success < 8:
        print("ERROR: too few relevant offline poster images were fetched.")
        sys.exit(1)

if __name__ == "__main__":
    main()
