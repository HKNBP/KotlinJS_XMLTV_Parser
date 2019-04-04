/*
 * HKNBP_Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HKNBP_Core is distributed in the hope that it will be useful,
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HKNBP_Core.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcekey.hknbp.hknbp_core

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.get
import org.w3c.xhr.XMLHttpRequest
import kotlin.js.Date

/**
 * 一條頻道嘅所有節目同相關資訊
 *
 * 此參考xmltv.dtd來編寫嘅Class
 * https://salsa.debian.org/nickm-guest/xmltv/blob/master/xmltv.dtd
 * */
open class XMLTV(
        val displayNames: MultiLanguage.MultiLanguageList<DisplayName>? = null,
        val icon: Icon?                                                 = null,
        val urls: ArrayList<String>?                                    = null,
        val programmes: Programme.ProgrammeList<Programme>?             = null
){
    interface MultiLanguage{
        val lang: String?

        class MultiLanguageList<T: MultiLanguage>: ArrayList<T>{
            private fun findSelectLanguage(lang: String?): String?{
                lang?: return null

                val langElementList = lang.split("-")
                var findSelectLanguageList: MultiLanguageList<T> = this
                for(index in 0 until langElementList.size){
                    val langElement = langElementList.getOrNull(index)
                    val findSelectLanguageListCache = MultiLanguageList<T>()
                    for(findSelectLanguageListElement in findSelectLanguageList){
                        val compareElement = findSelectLanguageListElement.lang?.split("-")?.getOrNull(index)
                        if(langElement?.toUpperCase() === compareElement?.toUpperCase()){
                            findSelectLanguageListCache.add(findSelectLanguageListElement)
                        }
                    }
                    findSelectLanguageList = findSelectLanguageListCache
                }

                return if(findSelectLanguageList.size == 0){ null }else{
                    findSelectLanguageList.getOrNull(0)?.lang
                }
            }

            private fun findSelectLanguage(langList: ArrayList<String?>): String?{
                for(lang in langList){
                    val selectLanguage = findSelectLanguage(lang)
                    if(selectLanguage != null){ return selectLanguage }
                }
                return null
            }

            /**
             * 以指定語言表獲取所有元素
             *
             * @param langList 指定語言嘅名嘅表, 由第0個為最優先輸出語言
             * @return 指定語言嘅所有元素, 但如果冇一個語言係同使用者語言一樣就用第0個語言
             * */
            fun getElementsByLanguage(langList: ArrayList<String?>): ArrayList<T>?{
                if(this.size < 1){ return null }
                val arrayList = ArrayList<T>()
                val selectLanguge = findSelectLanguage(langList)
                for (element in this){
                    if(selectLanguge === element.lang){ arrayList.add(element) }
                }
                return arrayList
            }

            /**
             * 以指定語言獲取所有元素
             *
             * @param langs 指定語言嘅名, 愈放得前嘅語言就愈優先輸出嘅語言
             * @return 指定語言嘅所有元素, 但如果冇一個語言係同使用者語言一樣就用第0個語言
             * */
            fun getElementsByLanguage(vararg langs: String?): ArrayList<T>?{
                val langList = ArrayList<String?>()
                for(lang in langs){langList.add(lang)}
                return getElementsByLanguage(langList)
            }

            /**
             * 以現在用家使用嘅語言獲取所有元素
             *
             * @return 指定語言嘅所有元素, 但如果冇一個語言係同使用者語言一樣就用第0個語言
             * */
            fun getElementsByLanguage(): ArrayList<T>?{
                val userLanguage: String = js("navigator.language || navigator.userLanguage;")

                return getElementsByLanguage(userLanguage)
            }

            constructor(): super()

            /**
             * @param initElements 初始化時一次過窒入所有元素
             * */
            constructor(vararg initElements: T): super() {
                for (initElement in initElements){
                    add(initElement)
                }
            }

            constructor(initialCapacity: Int): super(initialCapacity)

            constructor(elements: Collection<T>): super(elements)
        }
    }
    class DisplayName(
            override val lang: String?                      = null,
            val displayName: String?                        = null
    ): MultiLanguage
    class Icon(
            val src: String?                                = null,
            val width: Int?                                 = null,
            val height: Int?                                = null
    )
    class Programme(
            val start: Date,
            val stop: Date,
            val pdcStart: String?                                               = null,
            val vpsStart: String?                                               = null,
            val showView: String?                                               = null,
            val videoPlus: String?                                              = null,
            val clumpidx: String?                                               = null,
            val titles: MultiLanguage.MultiLanguageList<Title>?                 = null,
            val subTitles: MultiLanguage.MultiLanguageList<SubTitle>?           = null,
            val descs: MultiLanguage.MultiLanguageList<Desc>?                   = null,
            val credits: Credits?                                               = null,
            val date: String?                                                   = null,
            val categorys: MultiLanguage.MultiLanguageList<Category>?           = null,
            val keywords: MultiLanguage.MultiLanguageList<Keyword>?             = null,
            val languages: MultiLanguage.MultiLanguageList<Language>?           = null,
            val origLanguages: MultiLanguage.MultiLanguageList<OrigLanguage>?   = null,
            val length: Length?                                                 = null,
            val icon: Icon?                                                     = null,
            val urls: ArrayList<String>?                                        = null,
            val countrys: MultiLanguage.MultiLanguageList<Country>?             = null,
            val episodeNum: EpisodeNum?                                         = null,
            val video: Video?                                                   = null,
            val audio: Audio?                                                   = null,
            val previouslyShown: PreviouslyShown?                               = null,
            val premieres: MultiLanguage.MultiLanguageList<Premiere>?           = null,
            val lastChances: MultiLanguage.MultiLanguageList<LastChance>?       = null,
            val new: Boolean                                                    = false,
            val subtitles: Subtitles?                                           = null,
            val rating: Rating?                                                 = null,
            val starRating: StarRating?                                         = null,
            val reviews: MultiLanguage.MultiLanguageList<Review>?               = null
    ){
        class Title(
                override val lang: String?                      = null,
                val title: String?                              = null
        ): MultiLanguage
        class SubTitle(
                override val lang: String?                      = null,
                val subTitle: String?                           = null
        ): MultiLanguage
        class Desc(
                override val lang: String?                      = null,
                val desc: String?                               = null
        ): MultiLanguage
        class Credits(
                val directors: ArrayList<String>?               = null,
                val actors: ArrayList<Actor>?                   = null,
                val writers: ArrayList<String>?                 = null,
                val adapters: ArrayList<String>?                = null,
                val producers: ArrayList<String>?               = null,
                val composers: ArrayList<String>?               = null,
                val editors: ArrayList<String>?                 = null,
                val presenters: ArrayList<String>?              = null,
                val commentators: ArrayList<String>?            = null,
                val guests: ArrayList<String>?                  = null
        ){
            class Actor(
                    val role: String?                               = null,
                    val actor: String?                              = null
            )
        }
        class Category(
                override val lang: String?                      = null,
                val category: String?                           = null
        ): MultiLanguage
        class Keyword(
                override val lang: String?                      = null,
                val keyword: String?                            = null
        ): MultiLanguage
        class Language(
                override val lang: String?                      = null,
                val language: String?                           = null
        ): MultiLanguage
        class OrigLanguage(
                override val lang: String?                      = null,
                val origLanguage: String?                       = null
        ): MultiLanguage
        class Length(
                val units: String?                              = null,
                val length: String?                             = null
        )
        class Country(
                override val lang: String?                      = null,
                val country: String?                            = null
        ): MultiLanguage
        class EpisodeNum(
                val system: String?                             = null,
                val episodeNum: String?                         = null
        ){
            fun getSeason(): Int?{
                return when (system) {
                    "xmltv_ns" -> {
                        val number = episodeNum
                                ?.split(".")?.getOrNull(0)
                                ?.split("/")?.getOrNull(0)
                                ?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                        if(number != null){number + 1}else{number}
                    }
                    /** 暫時只支援xmltv_ns系統, 其他有待研究
                    "onscreen" -> { null }
                    "themoviedb.org" -> { null }
                    "thetvdb.com" -> { null }
                    "imdb.com" -> { null }
                    */
                    else -> {null}
                }
            }

            fun getTotalSeason(): Int?{
                return when (system) {
                    "xmltv_ns" -> {
                        episodeNum
                                ?.split(".")?.getOrNull(0)
                                ?.split("/")?.getOrNull(1)
                                ?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                    }
                    /** 暫時只支援xmltv_ns系統, 其他有待研究
                    "onscreen" -> { null }
                    "themoviedb.org" -> { null }
                    "thetvdb.com" -> { null }
                    "imdb.com" -> { null }
                    */
                    else -> {null}
                }
            }

            fun getEpisode(): Int?{
                return when (system) {
                    "xmltv_ns" -> {
                        val number = episodeNum
                                ?.split(".")?.getOrNull(1)
                                ?.split("/")?.getOrNull(0)
                                ?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                        if(number != null){number + 1}else{number}
                    }
                    /** 暫時只支援xmltv_ns系統, 其他有待研究
                    "onscreen" -> { null }
                    "themoviedb.org" -> { null }
                    "thetvdb.com" -> { null }
                    "imdb.com" -> { null }
                    */
                    else -> {null}
                }
            }

            fun getTotalEpisode(): Int?{
                return when (system) {
                    "xmltv_ns" -> {
                        episodeNum
                                ?.split(".")?.getOrNull(1)
                                ?.split("/")?.getOrNull(1)
                                ?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                    }
                    /** 暫時只支援xmltv_ns系統, 其他有待研究
                    "onscreen" -> { null }
                    "themoviedb.org" -> { null }
                    "thetvdb.com" -> { null }
                    "imdb.com" -> { null }
                    */
                    else -> {null}
                }
            }

            fun getPart(): Int?{
                return when (system) {
                    "xmltv_ns" -> {
                        val number = episodeNum
                                ?.split(".")?.getOrNull(2)
                                ?.split("/")?.getOrNull(0)
                                ?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                        if(number != null){number + 1}else{number}
                    }
                    /** 暫時只支援xmltv_ns系統, 其他有待研究
                    "onscreen" -> { null }
                    "themoviedb.org" -> { null }
                    "thetvdb.com" -> { null }
                    "imdb.com" -> { null }
                    */
                    else -> {null}
                }
            }

            fun getTotalPart(): Int?{
                return when (system) {
                    "xmltv_ns" -> {
                        episodeNum
                                ?.split(".")?.getOrNull(2)
                                ?.split("/")?.getOrNull(1)
                                ?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                    }
                    /** 暫時只支援xmltv_ns系統, 其他有待研究
                    "onscreen" -> { null }
                    "themoviedb.org" -> { null }
                    "thetvdb.com" -> { null }
                    "imdb.com" -> { null }
                    */
                    else -> {null}
                }
            }
        }
        class Video(
                val present: String?                            = null,
                val colour: String?                             = null,
                val aspect: String?                             = null,
                val quality: String?                            = null
        )
        class Audio(
                val present: String?                            = null,
                val stereo: String?                             = null
        )
        class PreviouslyShown(
                val start: Date?                                = null,
                val channel: String?                            = null
        )
        class Premiere(
                override val lang: String?                      = null,
                val premiere: String?                           = null
        ): MultiLanguage
        class LastChance(
                override val lang: String?                      = null,
                val lastChance: String?                         = null
        ): MultiLanguage
        class Subtitles(
                val type: String?                               = null,
                val language: String?                           = null
        )
        class Rating(
                val system: String?                             = null,
                val value: String?                              = null,
                val icon: Icon?                                 = null
        )
        class StarRating(
                val system: String?                             = null,
                val value: String?                              = null,
                val icon: Icon?                                 = null
        )
        class Review(
                val type: String?                               = null,
                val source: String?                             = null,
                val reviewer: String?                           = null,
                override val lang: String?                      = null,
                val review: String?                             = null
        ): MultiLanguage

        class ProgrammeList<T: Programme>: ArrayList<T>{
            /**
             * 獲取呢個時間節目資訊
             *
             * @param date 想獲取呢個時間節目嘅時間
             * @return 有關時間嘅節目
             * */
            fun getProgrammeByTime(date: Date): Programme?{
                for(programme in this){
                    if(programme.start <= date && date < programme.stop){
                        return programme
                    }
                }
                return null
            }

            /**
             * 獲取依家時間節目資訊
             *
             * @return 有關時間嘅節目
             * */
            fun getProgrammeByTime(): Programme?{
                return getProgrammeByTime(Date())
            }

            /**
             * 獲取呢個時間最接近嘅下個節目資訊
             *
             * @param date 想獲取某個時間最接近嘅下個節目嘅時間
             * @return 有關時間嘅節目
             * */
            fun getClosestNextProgrammeByTime(date: Date): Programme?{
                for(programme in this){
                    if(date <= programme.start){
                        return programme
                    }
                }
                return null
            }

            /**
             * 獲取呢個時間最接近嘅上個節目資訊
             *
             * @param date 想獲取某個時間最接近嘅上個節目嘅時間
             * @return 有關時間嘅節目
             * */
            fun getClosestPreviousProgrammeByTime(date: Date): Programme?{
                var i: Int = size - 1
                while(0 < i){
                    if(get(i).stop <= date){
                        return get(i)
                    }
                    i--
                }
                return null
            }

            constructor(): super(){
                sortBy { programme -> programme.start.getTime() }
            }

            /**
             * @param initElements 初始化時一次過窒入所有元素
             * */
            constructor(vararg initElements: T): super() {
                for (initElement in initElements){
                    add(initElement)
                }
            }

            constructor(initialCapacity: Int): super(initialCapacity){
                sortBy { programme -> programme.start.getTime() }
            }

            constructor(elements: Collection<T>): super(elements){
                sortBy { programme -> programme.start.getTime() }
            }
        }
    }

    companion object {
        /**
         * 解析XMLTV檔去獲取Channel資料
         *
         * @param xmltvSrc XMLTV檔地址
         * @param epgID 需要獲取嘅頻道嘅節目指南ID
         * @param onParseedXMLTVListener 當完成解析XMLTV就執行呢個function
         * */
        fun parseXMLTV(xmltvSrc: String, epgID: String, onParsedXMLTVListener: (xmltv: XMLTV) -> Unit, onFailedParseXMLTVListener: ()->Unit){
            LoadFile.load(xmltvSrc, fun(xmlHttp){
                onParsedXMLTVListener(getXMLTV(xmlHttp.responseXML, epgID))
            }, fun(){
                onFailedParseXMLTVListener()
            })
        }

        /**
         * 令Date可以進行比較
         *
         * @param date 要同呢個Date進行比教嘅對像Date
         * @return 如Int, >0即對像Date較細, <0即對像Date較大, =0即同對像Date時間相同
         * */
        operator fun Date.compareTo(date: Date): Int {
            return (getTime() - date.getTime()).toInt()
        }

        /**
         * 時區字串轉有用嘅時區數值
         *
         * @param timeZoneString 時區字串, 格式例如: "+0800" "+0900" "-0530" 由5個位組成 "ZZZZZ"
         * @return 有用嘅時區數值
         * */
        private fun timeZoneStringToTimeZone(timeZoneString: String): Int?{
            try {
                //檢查係米時間數值
                var timeZoneHour = (timeZoneString[1].toString()+timeZoneString[2].toString()).toDouble()
                var timeZoneMinute = (timeZoneString[3].toString()+timeZoneString[4].toString()).toDouble()
                if(24<timeZoneHour||60<timeZoneMinute){return null}

                //檢查timeZone係正定負
                when(timeZoneString[0]){
                    '+', '-' -> {
                        timeZoneHour*=(timeZoneString[0].toString()+"1").toDouble()
                        timeZoneMinute*=(timeZoneString[0].toString()+"1").toDouble()
                    }
                    else -> {return null}
                }

                return ((timeZoneHour + (timeZoneMinute / 60)) * 60).toInt()
            }catch (e: Exception){
                return null
            }
        }

        /**
         * 將Date轉換時區
         *
         * @param date 要轉時區嘅Date
         * @param timeZone 要轉到嘅時區數值
         * @return 轉換左時區嘅Date
         */
        private fun changeTimeZone(date: Date, timeZone: Int): Date {
            val utc = date.getTime() - (timeZone * 60 * 1000)
            return Date(utc + (-date.getTimezoneOffset()) * 60 * 1000)
        }

        /**
         * 將由String類型來記錄嘅Date轉成Date類型
         *
         * @param dateString 由String類型來記錄嘅Date
         * @return 已轉換成嘅Date類型嘅Date
         * */
        private fun dateStringToDate(dateString: String): Date? {
            try {
                val year = (dateString[0].toString() + dateString[1].toString() + dateString[2].toString() + dateString[3].toString()).toInt()
                val month = (dateString[4].toString() + dateString[5].toString()).toInt()
                val day = (dateString[6].toString() + dateString[7].toString()).toInt()
                val hour = (dateString[8].toString() + dateString[9].toString()).toInt()
                val minute = (dateString[10].toString() + dateString[11].toString()).toInt()
                val second = (dateString[12].toString() + dateString[13].toString()).toInt()
                val timeZone = dateString[15].toString() + dateString[16].toString() + dateString[17].toString() + dateString[18].toString() + dateString[19].toString()

                return changeTimeZone(Date(year, month-1, day, hour, minute, second), timeZoneStringToTimeZone(timeZone)?: 0)
            } catch (e: Exception) {
                return null
            }
        }

        private fun getXMLTV(xmltvDoc: Document?, epgID: String): XMLTV{
            var i = 0
            while(i < (xmltvDoc?.getElementsByTagName("channel")?.length ?: 0)) {
                if(xmltvDoc?.getElementsByTagName("channel")?.get(i)?.getAttribute("id") == epgID){
                    return XMLTV(
                            getDisplayNames(xmltvDoc.getElementsByTagName("channel").get(i)),
                            getIcon(xmltvDoc.getElementsByTagName("channel").get(i)),
                            getUrls(xmltvDoc.getElementsByTagName("channel").get(i)),
                            getProgrammes(xmltvDoc, epgID)
                    )
                }
                i++
            }
            return XMLTV()
        }

        private fun getDisplayNames(element: Element?): MultiLanguage.MultiLanguageList<DisplayName>?{
            if(element?.getElementsByTagName("displayName") == null){return null}

            val displayNames = MultiLanguage.MultiLanguageList<DisplayName>()

            var i = 0
            while(i < (element.getElementsByTagName("displayName").length)) {
                val lang        = getLang(element.getElementsByTagName("displayName").get(i))
                val displayName = element.getElementsByTagName("displayName").get(i)?.innerHTML
                displayNames.add(DisplayName(lang, displayName))
                i++
            }

            return displayNames
        }

        private fun getLang(element: Element?): String?{
            return element?.getAttribute("Lang")
        }

        private fun getIcon(element: Element?): Icon?{
            if(element?.getElementsByTagName("icon") == null){return null}

            return Icon(
                    getSrc(element.getElementsByTagName("icon").get(0)),
                    getWidth(element.getElementsByTagName("icon").get(0)),
                    getHeight(element.getElementsByTagName("icon").get(0))
            )
        }

        private fun getSrc(element: Element?): String?{
            return element?.getAttribute("src")
        }

        private fun getWidth(element: Element?): Int?{
            return element?.getAttribute("width")?.toIntOrNull()
        }

        private fun getHeight(element: Element?): Int?{
            return element?.getAttribute("height")?.toIntOrNull()
        }

        private fun getUrls(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("url") == null){return null}

            val urls = ArrayList<String>()

            var i = 0
            while(i < (element?.getElementsByTagName("url")?.length)) {
                val url = element?.getElementsByTagName("url")?.get(i)?.innerHTML

                if(url != null){ urls.add(url) }
                i++
            }

            return urls
        }

        private fun getProgrammes(xmltvDoc: Document?, epgID: String): Programme.ProgrammeList<Programme>?{
            if(xmltvDoc?.getElementsByTagName("programme") == null){return null}

            val programmes = Programme.ProgrammeList<Programme>()

            var i = 0
            while(i < (xmltvDoc.getElementsByTagName("programme").length)) {
                if(xmltvDoc.getElementsByTagName("programme").get(i)?.getAttribute("channel") == epgID){
                    val start           = getStart(xmltvDoc.getElementsByTagName("programme").get(i))
                    val stop            = getStop(xmltvDoc.getElementsByTagName("programme").get(i))
                    val pdcStart        = getPDCStart(xmltvDoc.getElementsByTagName("programme").get(i))
                    val vpsStart        = getVPSStart(xmltvDoc.getElementsByTagName("programme").get(i))
                    val showView        = getShowView(xmltvDoc.getElementsByTagName("programme").get(i))
                    val videoPlus       = getVideoPlus(xmltvDoc.getElementsByTagName("programme").get(i))
                    val clumpidx        = getClumpidx(xmltvDoc.getElementsByTagName("programme").get(i))
                    val titles          = getTitles(xmltvDoc.getElementsByTagName("programme").get(i))
                    val subTitles       = getSubTitles(xmltvDoc.getElementsByTagName("programme").get(i))
                    val descs           = getDescs(xmltvDoc.getElementsByTagName("programme").get(i))
                    val credits         = getCredits(xmltvDoc.getElementsByTagName("programme").get(i))
                    val date            = getDate(xmltvDoc.getElementsByTagName("programme").get(i))
                    val categorys       = getCategorys(xmltvDoc.getElementsByTagName("programme").get(i))
                    val keywords        = getKeywords(xmltvDoc.getElementsByTagName("programme").get(i))
                    val languages       = getLanguages(xmltvDoc.getElementsByTagName("programme").get(i))
                    val origLanguages   = getOrigLanguages(xmltvDoc.getElementsByTagName("programme").get(i))
                    val length          = getLength(xmltvDoc.getElementsByTagName("programme").get(i))
                    val icon            = getIcon(xmltvDoc.getElementsByTagName("programme").get(i))
                    val urls            = getUrls(xmltvDoc.getElementsByTagName("programme").get(i))
                    val countrys        = getCountrys(xmltvDoc.getElementsByTagName("programme").get(i))
                    val episodeNum      = getEpisodeNum(xmltvDoc.getElementsByTagName("programme").get(i))
                    val video           = getVideo(xmltvDoc.getElementsByTagName("programme").get(i))
                    val audio           = getAudio(xmltvDoc.getElementsByTagName("programme").get(i))
                    val previouslyShown = getPreviouslyShown(xmltvDoc.getElementsByTagName("programme").get(i))
                    val premieres       = getPremieres(xmltvDoc.getElementsByTagName("programme").get(i))
                    val lastChances     = getLastChances(xmltvDoc.getElementsByTagName("programme").get(i))
                    val new             = getNew(xmltvDoc.getElementsByTagName("programme").get(i))
                    val subtitles       = getSubtitles(xmltvDoc.getElementsByTagName("programme").get(i))
                    val rating          = getRating(xmltvDoc.getElementsByTagName("programme").get(i))
                    val starRating      = getStarRating(xmltvDoc.getElementsByTagName("programme").get(i))
                    val reviews         = getReviews(xmltvDoc.getElementsByTagName("programme").get(i))

                    if(start!=null && stop!=null && start<stop){//冇標明時間同時間錯誤將不加入到List中
                        programmes.add(Programme(
                                start, stop, pdcStart, vpsStart, showView, videoPlus, clumpidx, titles,
                                subTitles, descs, credits, date, categorys, keywords, languages, origLanguages,
                                length, icon, urls, countrys, episodeNum, video, audio, previouslyShown,
                                premieres, lastChances, new, subtitles, rating, starRating, reviews
                        ))
                    }
                }
                i++
            }
            programmes.sortBy{ programme -> programme.start.getTime() }
            return programmes
        }

        private fun getStart(element: Element?): Date?{
            return dateStringToDate(element?.getAttribute("start")?: "")
        }

        private fun getStop(element: Element?): Date?{
            return dateStringToDate(element?.getAttribute("stop")?: "")
        }

        private fun getPDCStart(element: Element?): String?{
            return element?.getAttribute("pdc-start")
        }

        private fun getVPSStart(element: Element?): String?{
            return element?.getAttribute("vps-start")
        }

        private fun getShowView(element: Element?): String?{
            return element?.getAttribute("showview")
        }

        private fun getVideoPlus(element: Element?): String?{
            return element?.getAttribute("videoplus")
        }

        private fun getClumpidx(element: Element?): String?{
            return element?.getAttribute("clumpidx")
        }

        private fun getTitles(element: Element?): MultiLanguage.MultiLanguageList<Programme.Title>?{
            if(element?.getElementsByTagName("title") == null){return null}

            val titles = MultiLanguage.MultiLanguageList<Programme.Title>()

            var i = 0
            while(i < (element?.getElementsByTagName("title")?.length)) {
                val lang  = getLang(element?.getElementsByTagName("title")?.get(i))
                val title = element?.getElementsByTagName("title")?.get(i)?.innerHTML

                titles.add(Programme.Title(lang, title))
                i++
            }

            return titles
        }

        private fun getSubTitles(element: Element?): MultiLanguage.MultiLanguageList<Programme.SubTitle>?{
            if(element?.getElementsByTagName("sub-title") == null){return null}

            val subTitles = MultiLanguage.MultiLanguageList<Programme.SubTitle>()

            var i = 0
            while(i < (element?.getElementsByTagName("sub-title")?.length)) {
                val lang        = getLang(element?.getElementsByTagName("sub-title")?.get(i))
                val subTitle    = element?.getElementsByTagName("sub-title")?.get(i)?.innerHTML

                subTitles.add(Programme.SubTitle(lang, subTitle))
                i++
            }

            return subTitles
        }

        private fun getDescs(element: Element?): MultiLanguage.MultiLanguageList<Programme.Desc>?{
            if(element?.getElementsByTagName("desc") == null){return null}

            val descs = MultiLanguage.MultiLanguageList<Programme.Desc>()

            var i = 0
            while(i < (element.getElementsByTagName("desc").length)) {
                val lang    = getLang(element.getElementsByTagName("desc").get(i))
                val desc    = element.getElementsByTagName("desc").get(i)?.innerHTML

                descs.add(Programme.Desc(lang, desc))
                i++
            }

            return descs
        }

        private fun getCredits(element: Element?): Programme.Credits?{
            if(element?.getElementsByTagName("credits") == null){return null}

            return Programme.Credits(
                    getDirectors(element.getElementsByTagName("credits").get(0)),
                    getActors(element.getElementsByTagName("credits").get(0)),
                    getWriters(element.getElementsByTagName("credits").get(0)),
                    getAdapters(element.getElementsByTagName("credits").get(0)),
                    getProducers(element.getElementsByTagName("credits").get(0)),
                    getComposers(element.getElementsByTagName("credits").get(0)),
                    getEditors(element.getElementsByTagName("credits").get(0)),
                    getPresenters(element.getElementsByTagName("credits").get(0)),
                    getCommentators(element.getElementsByTagName("credits").get(0)),
                    getGuests(element.getElementsByTagName("credits").get(0))
            )
        }

        private fun getDirectors(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("director") == null){return null}

            val directors = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("director").length)) {
                val director = element.getElementsByTagName("director").get(i)?.innerHTML

                if(director != null){ directors.add(director) }
                i++
            }

            return directors
        }

        private fun getActors(element: Element?): ArrayList<Programme.Credits.Actor>?{
            if(element?.getElementsByTagName("actor") == null){return null}

            val actors = ArrayList<Programme.Credits.Actor>()

            var i = 0
            while(i < (element.getElementsByTagName("actor").length)) {
                val role = getRole(element.getElementsByTagName("actor").get(i))
                val actor = element.getElementsByTagName("actor").get(i)?.innerHTML

                actors.add(Programme.Credits.Actor(role, actor))
                i++
            }

            return actors
        }

        private fun getRole(element: Element?): String?{
            return element?.getAttribute("rote")
        }

        private fun getWriters(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("writer") == null){return null}

            val writers = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("writer").length)) {
                val writer = element.getElementsByTagName("writer").get(i)?.innerHTML

                if(writer != null){ writers.add(writer) }
                i++
            }

            return writers
        }

        private fun getAdapters(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("adapter") == null){return null}

            val adapters = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("adapter").length)) {
                val adapter = element.getElementsByTagName("adapter").get(i)?.innerHTML

                if(adapter != null){ adapters.add(adapter) }
                i++
            }

            return adapters
        }

        private fun getProducers(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("producer") == null){return null}

            val producers = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("producer").length)) {
                val producer = element.getElementsByTagName("producer").get(i)?.innerHTML

                if(producer != null){ producers.add(producer) }
                i++
            }

            return producers
        }

        private fun getComposers(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("composer") == null){return null}

            val composers = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("composer").length)) {
                val composer = element.getElementsByTagName("composer").get(i)?.innerHTML

                if(composer != null){ composers.add(composer) }
                i++
            }

            return composers
        }

        private fun getEditors(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("editor") == null){return null}

            val editors = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("editor").length)) {
                val editor = element.getElementsByTagName("editor").get(i)?.innerHTML

                if(editor != null){ editors.add(editor) }
                i++
            }

            return editors
        }

        private fun getPresenters(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("presenter") == null){return null}

            val presenters = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("presenter").length)) {
                val presenter = element.getElementsByTagName("presenter").get(i)?.innerHTML

                if(presenter != null){ presenters.add(presenter) }
                i++
            }

            return presenters
        }

        private fun getCommentators(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("commentator") == null){return null}

            val commentators = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("commentator").length)) {
                val commentator = element.getElementsByTagName("commentator").get(i)?.innerHTML

                if(commentator != null){ commentators.add(commentator) }
                i++
            }

            return commentators
        }

        private fun getGuests(element: Element?): ArrayList<String>?{
            if(element?.getElementsByTagName("guest") == null){return null}

            val guests = ArrayList<String>()

            var i = 0
            while(i < (element.getElementsByTagName("guest").length)) {
                val guest = element.getElementsByTagName("guest").get(i)?.innerHTML

                if(guest != null){ guests.add(guest) }
                i++
            }

            return guests
        }

        private fun getDate(element: Element?): String?{
            return element?.getElementsByTagName("date")?.get(0)?.innerHTML
        }

        private fun getCategorys(element: Element?): MultiLanguage.MultiLanguageList<Programme.Category>?{
            if(element?.getElementsByTagName("category") == null){return null}

            val categorys = MultiLanguage.MultiLanguageList<Programme.Category>()

            var i = 0
            while(i < (element.getElementsByTagName("category").length)) {
                val lang = getLang(element.getElementsByTagName("category").get(i))
                val category = element.getElementsByTagName("category").get(i)?.innerHTML

                categorys.add(Programme.Category(lang, category))
                i++
            }

            return categorys
        }

        private fun getKeywords(element: Element?): MultiLanguage.MultiLanguageList<Programme.Keyword>?{
            if(element?.getElementsByTagName("keyword") == null){return null}

            val keywords = MultiLanguage.MultiLanguageList<Programme.Keyword>()

            var i = 0
            while(i < (element.getElementsByTagName("keyword").length)) {
                val lang = getLang(element.getElementsByTagName("keyword").get(i))
                val keyword = element.getElementsByTagName("keyword").get(i)?.innerHTML

                keywords.add(Programme.Keyword(lang, keyword))
                i++
            }

            return keywords
        }

        private fun getLanguages(element: Element?): MultiLanguage.MultiLanguageList<Programme.Language>?{
            if(element?.getElementsByTagName("language") == null){return null}

            val languages = MultiLanguage.MultiLanguageList<Programme.Language>()

            var i = 0
            while(i < (element.getElementsByTagName("language").length)) {
                val lang = getLang(element.getElementsByTagName("language").get(i))
                val language = element.getElementsByTagName("language").get(i)?.innerHTML

                languages.add(Programme.Language(lang, language))
                i++
            }

            return languages
        }

        private fun getOrigLanguages(element: Element?): MultiLanguage.MultiLanguageList<Programme.OrigLanguage>?{
            if(element?.getElementsByTagName("origLanguage") == null){return null}

            val origLanguages = MultiLanguage.MultiLanguageList<Programme.OrigLanguage>()

            var i = 0
            while(i < (element.getElementsByTagName("origLanguage").length)) {
                val lang = getLang(element.getElementsByTagName("origLanguage").get(i))
                val origLanguage = element.getElementsByTagName("origLanguage").get(i)?.innerHTML

                origLanguages.add(Programme.OrigLanguage(lang, origLanguage))
                i++
            }

            return origLanguages
        }

        private fun getLength(element: Element?): Programme.Length?{
            if(element?.getElementsByTagName("length") == null){return null}

            return Programme.Length(
                    getUnits(element.getElementsByTagName("length").get(0)),
                    element.getElementsByTagName("length").get(0)?.innerHTML
            )
        }

        private fun getUnits(element: Element?): String?{
            return element?.getAttribute("units")
        }

        private fun getCountrys(element: Element?): MultiLanguage.MultiLanguageList<Programme.Country>?{
            if(element?.getElementsByTagName("country") == null){return null}

            val countrys = MultiLanguage.MultiLanguageList<Programme.Country>()

            var i = 0
            while(i < (element.getElementsByTagName("country").length)) {
                val lang = getLang(element.getElementsByTagName("country").get(i))
                val country = element.getElementsByTagName("wcountry").get(i)?.innerHTML

                countrys.add(Programme.Country(lang, country))
                i++
            }

            return countrys
        }

        private fun getEpisodeNum(element: Element?): Programme.EpisodeNum?{
            if(element?.getElementsByTagName("episode-num") == null){return null}

            return Programme.EpisodeNum(
                    getSystem(element.getElementsByTagName("episode-num").get(0)),
                    element.getElementsByTagName("episode-num").get(0)?.innerHTML
            )
        }

        private fun getSystem(element: Element?): String?{
            return element?.getAttribute("system")
        }

        private fun getVideo(element: Element?): Programme.Video?{
            if(element?.getElementsByTagName("video") == null){return null}

            return Programme.Video(
                    getPresent(element.getElementsByTagName("video").get(0)),
                    getColour(element.getElementsByTagName("video").get(0)),
                    getAspect(element.getElementsByTagName("video").get(0)),
                    getQuality(element.getElementsByTagName("video").get(0))
            )
        }

        private fun getPresent(element: Element?): String?{
            return element?.getElementsByTagName("present")?.get(0)?.innerHTML
        }

        private fun getColour(element: Element?): String?{
            return element?.getElementsByTagName("colour")?.get(0)?.innerHTML
        }

        private fun getAspect(element: Element?): String?{
            return element?.getElementsByTagName("aspect")?.get(0)?.innerHTML
        }

        private fun getQuality(element: Element?): String?{
            return element?.getElementsByTagName("quality")?.get(0)?.innerHTML
        }

        private fun getAudio(element: Element?): Programme.Audio?{
            if(element?.getElementsByTagName("audio") == null){ return null }

            return Programme.Audio(
                    getPresent(element.getElementsByTagName("audio").get(0)),
                    getStereo(element.getElementsByTagName("audio").get(0))
            )
        }

        private fun getStereo(element: Element?): String?{
            return element?.getElementsByTagName("stereo")?.get(0)?.innerHTML
        }

        private fun getPreviouslyShown(element: Element?): Programme.PreviouslyShown?{
            if(element?.getElementsByTagName("previously-shown") == null){return null}

            return Programme.PreviouslyShown(
                    getStart(element.getElementsByTagName("previously-shown").get(0)),
                    getChannel(element.getElementsByTagName("previously-shown").get(0))
            )
        }

        private fun getChannel(element: Element?): String?{
            return element?.getElementsByTagName("channel")?.get(0)?.innerHTML
        }

        private fun getPremieres(element: Element?): MultiLanguage.MultiLanguageList<Programme.Premiere>?{
            if(element?.getElementsByTagName("premiere") == null){return null}

            val premieres = MultiLanguage.MultiLanguageList<Programme.Premiere>()

            var i = 0
            while(i < (element.getElementsByTagName("premiere").length)) {
                val lang = getLang(element.getElementsByTagName("premiere").get(i))
                val premiere = element.getElementsByTagName("premiere").get(i)?.innerHTML

                premieres.add(Programme.Premiere(lang, premiere))
                i++
            }

            return premieres
        }

        private fun getLastChances(element: Element?): MultiLanguage.MultiLanguageList<Programme.LastChance>?{
            if(element?.getElementsByTagName("last-chance") == null){return null}

            val lastChances = MultiLanguage.MultiLanguageList<Programme.LastChance>()

            var i = 0
            while(i < (element.getElementsByTagName("last-chance").length)) {
                val lang = getLang(element.getElementsByTagName("last-chance").get(i))
                val lastChance = element.getElementsByTagName("last-chance").get(i)?.innerHTML

                lastChances.add(Programme.LastChance(lang, lastChance))
                i++
            }

            return lastChances
        }

        private fun getNew(element: Element?): Boolean{
            return element?.getElementsByTagName("new")?.get(0) != null
        }

        private fun getSubtitles(element: Element?): Programme.Subtitles?{
            if(element?.getElementsByTagName("video") == null){ return null }

            return Programme.Subtitles(
                    getType(element.getElementsByTagName("video").get(0)),
                    getLanguage(element.getElementsByTagName("video").get(0))
            )
        }

        private fun getType(element: Element?): String?{
            return element?.getAttribute("type")
        }

        private fun getLanguage(element: Element?): String?{
            return element?.getElementsByTagName("language")?.get(0)?.innerHTML
        }

        private fun getRating(element: Element?): Programme.Rating?{
            if(element?.getElementsByTagName("rating") == null){ return null }

            return Programme.Rating(
                    getSystem(element.getElementsByTagName("rating").get(0)),
                    getValue(element.getElementsByTagName("rating").get(0)),
                    getIcon(element.getElementsByTagName("rating").get(0))
            )
        }

        private fun getValue(element: Element?): String?{
            return element?.getAttribute("value")
        }

        private fun getStarRating(element: Element?): Programme.StarRating?{
            if(element?.getElementsByTagName("star-rating") == null){ return null }

            return Programme.StarRating(
                    getSystem(element.getElementsByTagName("star-rating").get(0)),
                    getValue(element.getElementsByTagName("star-rating").get(0)),
                    getIcon(element.getElementsByTagName("star-rating").get(0))
            )
        }

        private fun getReviews(element: Element?): MultiLanguage.MultiLanguageList<Programme.Review>?{
            if(element?.getElementsByTagName("review") == null){ return null }

            val reviews = MultiLanguage.MultiLanguageList<Programme.Review>()

            var i = 0
            while(i < (element.getElementsByTagName("review").length)) {
                val type = getType(element.getElementsByTagName("review").get(i))
                val source = getSource(element.getElementsByTagName("review").get(i))
                val reviewer = getReviewer(element.getElementsByTagName("review").get(i))
                val lang = getLang(element.getElementsByTagName("review").get(i))
                val review = element.getElementsByTagName("review").get(i)?.innerHTML

                reviews.add(Programme.Review(type, source, reviewer, lang, review))
                i++
            }
            return reviews
        }

        private fun getSource(element: Element?): String?{
            return element?.getAttribute("source")
        }

        private fun getReviewer(element: Element?): String?{
            return element?.getAttribute("reviewer")
        }
    }
}