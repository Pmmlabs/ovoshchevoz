import requests
from collections import Counter
from bs4 import BeautifulSoup


def run(url, url_song, song_selector,text_selector,filename):
    session = requests.Session()
    response = session.request("GET", url=url, verify=False)
    soup = BeautifulSoup(response.content, 'lxml')
    links = soup.select(song_selector)
    split_it = []
    with open(filename, 'w', encoding='utf-8') as file:
        for link in links:
            response = session.request("GET", url=url_song + link.attrs['href'], verify=False)
            page = BeautifulSoup(response.content, 'lxml')
            paragraph = page.select_one(text_selector)
            if "I" not in paragraph.text:
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
                                    .lower()
                                    .split())

        counter = Counter(split_it)
        most_occur = counter.most_common(5000)
        for (word, frequency) in most_occur:
            if frequency > 2 and len(word)>2:
                file.write(word + '\n')
        file.close()


run("https://nashestviefest.ru/diskoteka-avarija","https://nashestviefest.ru", '.album  ul > li > a','.text-original',"discoteka_avaria.txt")
# run("https://nashestviefest.ru/arija","https://nashestviefest.ru", '.album  ul > li > a','.text-original',"aria.txt")
# run("https://altwall.net/texts.php?show=slot","https://altwall.net", ".texts_table tr a",'#main_text_div > p',"slot.txt")
