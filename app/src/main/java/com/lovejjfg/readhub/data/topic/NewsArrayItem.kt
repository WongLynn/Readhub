/*
 *
 *   Copyright (c) 2017.  Joe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.lovejjfg.readhub.data.topic

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class NewsArrayItem(

	@field:SerializedName("authorName")
	val authorName: String? = null,

	@field:SerializedName("groupId")
	val groupId: Int? = null,

	@field:SerializedName("duplicateId")
	val duplicateId: Int? = null,

	@field:SerializedName("publishDate")
	val publishDate: String? = null,

	@field:SerializedName("siteName")
	val siteName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("mobileUrl")
	val mobileUrl: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)