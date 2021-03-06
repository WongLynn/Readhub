/*
 * Copyright (c) 2017.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.readhub.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.InstantView
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.databinding.ActivityInstantDetailBinding
import com.lovejjfg.readhub.databinding.HolderImgParseBinding
import com.lovejjfg.readhub.utils.JsoupUtils
import com.lovejjfg.readhub.utils.UIUtil
import com.lovejjfg.readhub.view.holder.ImageParseHolder
import com.lovejjfg.readhub.view.holder.TextParseHolder
import com.lovejjfg.readhub.view.recycerview.ParseItemDerection
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import org.jsoup.Jsoup

/**
 * Created by joe on 2017/9/28.
 * Email: lovejjfg@gmail.com
 */
class InstantActivity : BaseActivity() {
    private var instantbind: ActivityInstantDetailBinding? = null
    private var refresh: SwipeRefreshLayout? = null
    private var topicDetailAdapter: InstantAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val topicId = intent.getStringExtra(Constants.ID)
        if (TextUtils.isEmpty(topicId)) {
            finish()
            return
        }
        instantbind = DataBindingUtil.setContentView(this, R.layout.activity_instant_detail)
        refresh = instantbind?.container
        refresh?.setOnRefreshListener {
            getData(topicId)
        }
        refresh?.isRefreshing = true
        instantbind?.toolbar?.setNavigationOnClickListener({ finish() })
        val rvHot = instantbind?.rvDetail
        instantbind?.toolbar?.setOnClickListener {
            if (UIUtil.doubleClick()) {
                rvHot?.smoothScrollToPosition(0)
            }
        }
        rvHot?.addItemDecoration(ParseItemDerection())
        rvHot?.layoutManager = LinearLayoutManager(this)
        topicDetailAdapter = InstantAdapter()
        topicDetailAdapter!!.attachRecyclerView(rvHot!!)
        getData(topicId)

    }

    private fun getData(topicId: String) {
        DataManager.subscribe(DataManager.init().topicInstant(topicId), Consumer {
            handleInstant(it)
        }, Consumer {
            it.printStackTrace()
            refresh?.isRefreshing = false
        })
    }

    private fun handleInstant(instantView: InstantView?) {
        refresh?.isRefreshing = false
        instantbind?.toolbar?.title = instantView?.title
        if (instantView == null) {
            Log.e("InstantActivity", "extra is null")
            return
        }
        Observable.create<DetailItems> {
            val body = Jsoup.parse(instantView.content).body().children()
            JsoupUtils.parse(body, it, "", 0)
            it.onComplete()
        }
                .toList()
                .subscribe({
                    topicDetailAdapter!!.clearList()
                    topicDetailAdapter!!.setList(it)
                }, { it.printStackTrace() })

    }

    inner class InstantAdapter : PowerAdapter<DetailItems>() {
        override fun getItemViewTypes(position: Int): Int {
            return list[position].type!!
        }

        override fun onViewHolderBind(holder: PowerHolder<DetailItems>?, position: Int) {
            holder!!.onBind(list[position])
        }

        override fun onViewHolderCreate(parent: ViewGroup?, viewType: Int): PowerHolder<DetailItems> {
            return when (viewType) {
                Constants.TYPE_PARSE_TEXT -> {
                    TextParseHolder(UIUtil.inflate(R.layout.holder_text_parse, parent!!))
                }
                else -> {
                    val imgParseBinding = DataBindingUtil.inflate<HolderImgParseBinding>(LayoutInflater.from(parent?.context), R.layout.holder_img_parse, parent, false)

                    ImageParseHolder(imgParseBinding)
                }
            }
        }

    }
}