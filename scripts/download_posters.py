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
WIKIPEDIA_PAGES = {
    "poster_nirvana": "Nirvana (band)",
    "poster_nevermind": "Nevermind",
    "poster_inutero": "In Utero",
    "poster_grunge": "Grunge",
    "poster_pink_floyd": "Pink Floyd",
    "poster_dark_side": "The Dark Side of the Moon",
    "poster_daftpunk": "Daft Punk",
    "poster_discovery": "Discovery (Daft Punk album)",
    "poster_interstella": "Interstella 5555",
    "poster_cinema": "Cinema",
    "poster_apollo11": "Apollo 11",
    "poster_voyager": "Voyager program",
    "poster_design_history": "History of graphic design",
}
POSTERS = {
    "poster_nirvana": ["Kurt Cobain Nirvana band", "Nirvana band"],
    "poster_nevermind": ["Nevermind Nirvana album cover", "Nirvana band Kurt Cobain"],
    "poster_inutero": ["In Utero Nirvana album cover", "Nirvana band 1993"],
    "poster_seattle": ["Seattle skyline city"],
    "poster_grunge": ["grunge music band Seattle", "Nirvana Soundgarden Pearl Jam"],
    "poster_pink_floyd": ["Pink Floyd band members", "Pink Floyd live concert"],
    "poster_dark_side": ["Pink Floyd prism Dark Side of the Moon"],
    "poster_daftpunk": ["Daft Punk musicians helmets", "Daft Punk live"],
    "poster_discovery": ["Daft Punk Discovery album cover", "Daft Punk live electronic music"],
    "poster_interstella": ["Interstella 5555 movie poster", "Leiji Matsumoto anime"],
    "poster_cinema": ["cinema film camera movie camera"],
    "poster_apollo11": ["Apollo 11 moon landing astronauts"],
    "poster_voyager": ["Voyager 1 spacecraft NASA", "Voyager spacecraft NASA"],
    "poster_design_history": ["Bauhaus graphic design poster", "graphic design history poster"],
}

def request_json(url):
    req = urllib.request.Request(url, headers={"User-Agent": UA, "Accept": "application/json"})
    with urllib.request.urlopen(req, timeout=25) as response:
        return json.loads(response.read().decode("utf-8"))

def wikipedia_image(page_title):
    slug = urllib.parse.quote(page_title.replace(" ", "_"), safe="()_")
    url = "https://en.wikipedia.org/api/rest_v1/page/summary/" + slug
    data = request_json(url)
    image = (data.get("originalimage") or {}).get("source") or (data.get("thumbnail") or {}).get("source") or ""
    page_url = ((data.get("content_urls") or {}).get("desktop") or {}).get("page", "")
    if image.startswith("https://"):
        return image, page_title, page_url, {}
    return None

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
        page_title = WIKIPEDIA_PAGES.get(filename)
        if page_title:
            try:
                found = wikipedia_image(page_title)
                if found:
                    url, title, page_url, metadata = found
                    download(url, path)
                    credits.append("%s | Wikipedia: %s | %s" % (filename, title, page_url))
                    success += 1
                    found_image = True
                    print("Downloaded %s (%d bytes) from Wikipedia: %s" % (filename, os.path.getsize(path), title))
            except Exception as error:
                if os.path.exists(path):
                    os.remove(path)
                print("WARNING: Wikipedia image failed for %s: %s" % (filename, error))
        for query in ([] if found_image else queries):
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
