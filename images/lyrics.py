#!/usr/bin/python
# -*- coding: utf-8 -*-

import requests
from collections import Counter
from bs4 import BeautifulSoup


def run(url, url_song, song_selector,text_selector,filename):
    session = requests.Session()
    response = session.request("GET", url=url, verify=False)
    soup = BeautifulSoup(response.content, 'lxml')
    links = soup.select(song_selector)
    split_it = []
    i = 0
    with open(filename, 'w', encoding='utf-8') as file:
        for link in links:
            print(f"{i}/{len(links)}: {link.attrs['href']}")
            i=i+1
            if "remix" in link.attrs['href']:
                continue
            response = session.request("GET", url=url_song + link.attrs['href'], verify=False)
            page = BeautifulSoup(response.content, 'lxml')
            paragraph = page.select_one(text_selector)
            if True: #"I" not in paragraph.text:
                # file.writelines([s.text+'\n' for s in paragraph.strings])
                for s in paragraph.strings:
                    split_it.extend(s.text
                                    .replace(',', '')
                                    .replace('.', '')
                                    .replace('!', '')
                                    .replace('?', '')
                                    .replace(':', '')
                                    .replace('"', '')
                                    .replace(')', '')
                                    .replace('(', '')
                                    .replace('…', '')
                                    .replace('[', '')
                                    .replace(']', '')
                                    .replace("'", '')
                                    .replace("»", '')
                                    .replace("«", '')
                                    .lower()
                                    .split())

        counter = Counter(split_it)
        most_occur = counter.most_common(5000)
        for (word, frequency) in most_occur:
            if frequency > 2 and len(word)>2:
                file.write(word + '\n')
        file.close()


run("http://www.songlyrics.com/lady-gaga-lyrics/","", 'table.tracklist:nth-child(3) > tbody:nth-child(1) > tr > td > a','#songLyricsDiv',
    "../app/src/main/res/raw/gaga.txt")
#run("https://nashestviefest.ru/korole-i-sut","https://nashestviefest.ru", '.album  ul > li > a','.text-original',
#    "../app/src/main/res/raw/kish.txt")
#run("https://nashestviefest.ru/diskoteka-avarija","https://nashestviefest.ru", '.album  ul > li > a','.text-original',"discoteka_avaria.txt")
# run("https://nashestviefest.ru/arija","https://nashestviefest.ru", '.album  ul > li > a','.text-original',"aria.txt")
# run("https://altwall.net/texts.php?show=slot","https://altwall.net", ".texts_table tr a",'#main_text_div > p',"slot.txt")
