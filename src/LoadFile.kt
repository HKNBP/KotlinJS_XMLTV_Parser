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

import jquery.jq
import org.w3c.dom.*
import org.w3c.dom.parsing.DOMParser
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document

object LoadFile {
    fun load(filePath: String, onLoadedFile: (xmlhttp: XMLHttpRequest)->Unit, onFailedLoadFile: ()->Unit){
        val xmlhttp = XMLHttpRequest()
        var isLoaded = false
        xmlhttp.onreadystatechange = fun(event) {
            if(!isLoaded){
                if (xmlhttp.readyState == 4.toShort() && xmlhttp.status == 200.toShort()) {
                    isLoaded = true
                    onLoadedFile(xmlhttp)
                }
            }
        }
        var isFailedLoad = false
        xmlhttp.ontimeout = fun(event){
            if(!isFailedLoad){
                isFailedLoad = true
                onFailedLoadFile()
                //PromptBox.promptMessage(dialogues.node().canNotReadData)
            }
        }
        xmlhttp.onerror = fun(event){
            if(!isFailedLoad){
                isFailedLoad = true
                onFailedLoadFile()
                //PromptBox.promptMessage(dialogues.node().canNotReadData)
            }
        }
        xmlhttp.open("GET", filePath, true)
        xmlhttp.send()
    }

    fun load(filePath: String): XMLHttpRequest{
        val xmlhttp = XMLHttpRequest()
        xmlhttp.open("GET", filePath, false)
        xmlhttp.send()
        return xmlhttp
    }
}