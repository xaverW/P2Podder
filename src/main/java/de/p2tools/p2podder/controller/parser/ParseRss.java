/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */


package de.p2tools.p2podder.controller.parser;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.Download;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import org.jdom.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseRss {

    private ParseRss() {
    }

    public static void parse(ProgData progData, Podcast podcast) {
        try {
//            URL feedUrl = new URL("https://www.deutschlandfunk.de/podcast-deutschlandfunk-der-tag.3417.de.podcast.xml");
            URL feedUrl = new URL(podcast.getUrl());


            SyndFeedInput input = new SyndFeedInput();
            SyndFeed syndFeed = input.build(new XmlReader(feedUrl));

//        <item>
//        <title>Aiwangers Impfskespis - Bayerns Koalition unter Druck - Der Tag</title>
//        <link>https://www.deutschlandfunk.de/der-tag-aiwangers-impfskespis-bayerns-koalition-unter-druck.3415.de.html?dram:article_id=501000</link>
//        <description><![CDATA[<img src="https://www.deutschlandfunk.de/media/thumbs/3/31d944f8d32640199d2da143d3de472bv1_max_144x81_b3535db83dc50e27c1bb1392364c95a2.jpg?key=e784df" alt="Hubert Aiwanger (l, Freie Wähler), stellvertretender Ministerpräsident und Staatsminister für Wirtschaft, Landentwicklung und Energie, und Markus Söder (r, CSU), Ministerpräsident von Bayern, gehen gemeinsam nach der Kabinettssitzung, die im Hofgarten des Regierungssitz stattfand, zu einer abschließenden Pressekonferenz. Im Mittelpunkt stand erneut die Corona-Krise. Unter anderem hat der Ministerrat entscheiden, wie es bei den ffnungen von Bars und Kneipen weitergehen soll. (picture alliance / dpa / Peter Kneffel)" title="Hubert Aiwanger (l, Freie Wähler), stellvertretender Ministerpräsident und Staatsminister für Wirtschaft, Landentwicklung und Energie, und Markus Söder (r, CSU), Ministerpräsident von Bayern, gehen gemeinsam nach der Kabinettssitzung, die im Hofgarten des Regierungssitz stattfand, zu einer abschließenden Pressekonferenz. Im Mittelpunkt stand erneut die Corona-Krise. Unter anderem hat der Ministerrat entscheiden, wie es bei den ffnungen von Bars und Kneipen weitergehen soll. (picture alliance / dpa / Peter Kneffel)" width="144" height="81" border="0" align="left" hspace="4" vspace="4"/>Die Positionen zum Impfen könnten nicht gegensätzlicher sein: Markus Söder wirbt dafür, Hubert Aiwanger will sich bis auf weiteres nicht impfen lassen. Welche Auswirkungen hat das auf die Koalition von CSU und Freien Wählern in Bayern? Außerdem: BGH Urteil zu Facebook. Das Unternehmen darf User*innenkommentare löschen und Konten sperren - aber nur unter bestimmten Voraussetzungen.  <br clear="all"/> <br/>Von Sonja Meschkat <br/>www.deutschlandfunk.de, Deutschlandfunk - Der Tag <br/>Hören bis: 19.01.2038 04:14 <br/><a href="https://podcast-mp3.dradio.de/podcast/2021/07/29/290721_aiwangers_impfskespis_bayerns_koalition_unter_dlf_20210729_1700_4676ae16.mp3">Direkter Link zur Audiodatei</a> <br/><p> <br/></p>]]></description>
//        <guid>https://podcast-mp3.dradio.de/podcast/2021/07/29/290721_aiwangers_impfskespis_bayerns_koalition_unter_dlf_20210729_1700_4676ae16.mp3</guid>
//        <pubDate>Thu, 29 Jul 2021 17:00:00 +0200</pubDate>
//        <enclosure url="https://podcast-mp3.dradio.de/podcast/2021/07/29/290721_aiwangers_impfskespis_bayerns_koalition_unter_dlf_20210729_1700_4676ae16.mp3" length="20725734" type="audio/mpeg"/>
//        <itunes:author>Meschkat, Sonja</itunes:author>
//        <itunes:duration>21:38</itunes:duration>
//        </item>


            ArrayList<Download> downloads = new ArrayList<>();

            List<SyndEntry> entries = syndFeed.getEntries();
            for (SyndEntry syndEntry : entries) {
//                Module entryModule = syndEntry.getModule("http://www.itunes.com/dtds/podcast-1.0.dtd");

                String author = syndEntry.getAuthor();
                String title = syndEntry.getTitle();
                String uri = syndEntry.getUri();
                String website = syndEntry.getLink();
                String pubDate = syndEntry.getPublishedDate().toString();
                String duration = "";
                SyndContent desc = syndEntry.getDescription();
                final List<Element> foreignMarkup = (List<Element>) syndEntry.getForeignMarkup();
                for (Element element : foreignMarkup) {
                    if (element.getName().equals("duration")) {
                        duration = element.getValue();
                    }
                }

                String description = "";
                if (desc != null) {
                    description = desc.getValue();
                    description = cleanDescription(description);
                }
                long size = 0;
                List<SyndEnclosureImpl> enclosures = (List) syndEntry.getEnclosures();
                if (enclosures != null) {
                    //if in the enclosure list is a media type (either audio or video), this will set as the link of the episode
                    for (SyndEnclosureImpl enclosure : enclosures) {
                        if (null != enclosure) {
                            uri = enclosure.getUrl();
                            size = enclosure.getLength();
//                            String s = enclosure.getType();
//                            System.out.println("Length: " + l + " Type: " + s + " URL: " + enclosure.getUrl());
                            break;
                        }
                    }
                }

                Download download = new Download(uri, title, website, pubDate, description, podcast);
                download.getPdownloadSize().setFileSize(size);
                download.setDuration(duration);

                downloads.add(download);
//                System.out.println("author : " + author);
//                System.out.println("Title : " + title);
            }
            if (!downloads.isEmpty()) {
                progData.downloadList.addNewDownloads(downloads);
            }
        } catch (Exception ex) {
            PLog.errorLog(897451209, ex.getMessage());
        }
    }

    //<img src="https://assets.2" alt="Di+" title="Die Frt+++" width="144" height="81" border="0" align="left" hspace="4" vspace="4"/>
    //<a title="" href="https://podcast-mp3.dradio.de/podcast/2022/05/27/china_und_die_deutsche_wirtschaft_dlf_20220527_1700_9edfa0d3.mp3"></a>
    //<p></p>

    static final Pattern pImg = Pattern.compile("<img[^>]*src=\"[^\"]*\"[^>]*>", Pattern.CASE_INSENSITIVE);

    private static String cleanDescription(String description) {
        String ret = description;

        Matcher m = pImg.matcher(ret);
        while (m.find()) {
            ret = m.replaceAll("");
        }

        ret = ret.replaceAll("<br/>", P2LibConst.LINE_SEPARATOR);
        ret = ret.replaceAll("<br />", P2LibConst.LINE_SEPARATOR);
        ret = ret.replaceAll("<BR/>", P2LibConst.LINE_SEPARATOR);
        ret = ret.replaceAll("<BR />", P2LibConst.LINE_SEPARATOR);
        ret = ret.replaceAll("<p>", "");
        ret = ret.replaceAll("</p>", "");
        ret = ret.replaceAll("Direkter Link zur Audiodatei", "");
        ret = ret.replaceAll("\\<.*?>", "");

        while (ret.startsWith(P2LibConst.LINE_SEPARATOR)) {
            ret = ret.replaceFirst(P2LibConst.LINE_SEPARATOR, "");
        }
        ret = ret.trim();
        return ret;
    }
}
