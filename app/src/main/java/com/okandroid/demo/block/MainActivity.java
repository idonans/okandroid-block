package com.okandroid.demo.block;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.okandroid.block.core.StorageManager;
import com.okandroid.block.data.TmpFileManager;
import com.okandroid.block.lang.GBKLengthInputFilter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.item_content)
    TextView mItemContent;

    @BindView(R.id.fullscreen_toggle)
    TextView mFullscreenToggle;

    @BindView(R.id.start_browser)
    View mStartBrowser;

    @BindView(R.id.editText)
    EditText mEditText;

    @BindView(R.id.test_ipc)
    View mTestIPC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (savedInstanceState == null) {
            TmpFileManager.getInstance().clear();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mItemContent.setText("hello, butter knife");
        RxView.clicks(mFullscreenToggle)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                if (mFullscreen) {
                                    requestExitFullscreen();
                                } else {
                                    requestFullscreen();
                                }
                            }
                        });

        RxView.clicks(mStartBrowser)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                startActivity(BrowserActivity.startIntent(MainActivity.this));
                            }
                        });

        mEditText.setFilters(new InputFilter[]{new GBKLengthInputFilter(10, true)});

        RxView.clicks(mTestIPC)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                testIPC();
                            }
                        });
    }

    private boolean mFullscreen;

    private void requestFullscreen() {
        mFullscreen = true;

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    private void requestExitFullscreen() {
        mFullscreen = false;

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void testIPC() {
        String key = "test_ipc_huge_text_3";
        String value = "{\"code\":0,\"data\":{\"articleImageList\":[],\"qrcode\":\"community/0796c55acc82e10000013ef6b8eecd.jpg\",\"sharewords\":\"大幅度放宽市场准入”“加强知识产权保护”“降低汽车进口关税”……4月10日，习近平在博鳌亚洲论坛2018年年会开幕式上宣布了中国扩大开放的一系列新举措，并再次明确宣示：中国开放的大门不会关闭，只会越开越大！\",\"memolist\":[[{\"type\":\"text\",\"content\":\"大幅度放宽市场准入”“加强知识产权保护”“降低汽车进口关税”……4月10日，习近平在博鳌亚洲论坛2018年年会开幕式上宣布了中国扩大开放的一系列新举措，并再次明确宣示：中国开放的大门不会关闭，只会越开越大！\"}],[],[{\"type\":\"text\",\"content\":\"实打实的举措和表态，收获了现场的掌声和舆论的赞誉。然而，在中美贸易摩擦激烈的背景下，也有人疑虑：中国扩大开放的新举措是不得已而为之。\"}],[],[{\"type\":\"text\",\"content\":\"事实真是这样吗？当然不是！\"}],[],[{\"type\":\"strong\",\"content\":\"1.从大幅度放宽市场准入到主动扩大进口，中国早有安排\"}],[],[{\"type\":\"text\",\"content\":\"对于当今世界，中国一直坚持自己的判断：经济全球化是不可逆转的时代潮流。\"}],[],[{\"type\":\"text\",\"content\":\"基于这一判断，中国这些年开放力度越来越大，具体举措不断出台。而这些都完全是在我们一贯政策主张基础上推出的，都是立足自身发展、按照自己的节奏进行的。\"}],[],[{\"type\":\"text\",\"content\":\"一个显而易见的事实是，过去40年，对外开放是中国的一项长期基本国策。根据自身情况，中国一直按照自己扩大开放的时间表和路线图循序渐进。十八大以来，中国扩大对外开放的新举措不断出台，开放步伐越来越快。十九大报告同样强调，要“坚持打开国门搞建设”。\"}],[],[{\"type\":\"text\",\"content\":\"具体到今年进一步扩大开放，同样“有章可循”。去年年底的中央经济工作会议就提出，要在开放的范围和层次上进一步拓展。\"}],[],[{\"type\":\"text\",\"content\":\"今年全国两会上，政府工作报告就提出，要扩大电信、医疗、教育、养老等领域开放，下调汽车、部分日用消费品等进口关税。甚至连特朗普近日频频亮出的知识产权问题，这份报告也早已提及：“强化知识产权保护，实行侵权惩罚性赔偿制度。”\"}],[],[{\"type\":\"strong\",\"content\":\"上述文件和报告白纸黑字，早已公布。中国立足自身发展需要，自行推出扩大开放新举措，但有些人非要把中国的战略抉择和当下的中美贸易摩擦背景挂钩解读，这种罔顾事实的自行脑补，可以休矣！\"}],[],[{\"type\":\"strong\",\"content\":\"2.中国开放的大门只会越开越大，但别指望中国会在外部压力下门洞大开\"}],[],[{\"type\":\"text\",\"content\":\"“我要明确告诉大家，中国开放的大门不会关闭，只会越开越大！”习近平在论坛上明确表态，再一次宣示了中国扩大开放的决心。他还同时点明：中国不以追求贸易顺差为目标，真诚希望扩大进口，促进经常项目收支平衡。\"}],[],[{\"type\":\"text\",\"content\":\"这份真诚，人们足以感受到。\"}],[],[{\"type\":\"text\",\"content\":\"目前，我国已与24个国家和地区签署了16个自由贸易协定，已生效实施15个，涵盖8000余种零关税进口产品。\"}],[],[{\"type\":\"text\",\"content\":\"自贸区协定之外，从2017年12月1日起，我国调降187项商品的进口关税，平均税率由17.3%降至7.7%。\"}],[],[{\"type\":\"text\",\"content\":\"据世贸组织统计，我国进口增速明显高于全球平均水平和美、德、日等国。而且，未来5年，中国将预计进口超过10万亿美元的商品和服务。\"}]],\"articledata\":{\"articleCates\":[{\"id\":607,\"name\":\"网页\",\"nameEn\":\"website\"}],\"articleTags\":[],\"cate\":695,\"cateObj\":{\"id\":695,\"name\":\"教程\",\"nameEn\":\"original/translated materials\"},\"channel\":0,\"commentCount\":10,\"commentCountStr\":\"0\",\"copyright\":0,\"cover\":\"http://img.zcool.cn/community/0146ca5acc82c9c9742719442b4b77.jpeg\",\"coverName\":\"0146ca5acc82c9c9742719442b4b77.jpeg\",\"coverPath\":\"community\",\"createTime\":1523352100000,\"creator\":15709589,\"creatorObj\":{\"avatar\":\"http://img.zcool.cn/community/00dc195acc566fc9742719442c173e.jpg\",\"avatar1x\":\"http://img.zcool.cn/community/00dc195acc566fc9742719442c173e.jpg@80w_80h_1c_1e_1o_100sh.jpg\",\"avatar2x\":\"http://img.zcool.cn/community/00dc195acc566fc9742719442c173e.jpg@160w_160h_1c_1e_1o_100sh.jpg\",\"city\":3053,\"cityName\":\"石家庄\",\"contentCount\":4,\"contentCountStr\":\"4\",\"contentCountTips\":\"共创作4组作品/文章\",\"contentPageUrl\":\"http://www.test.zcool.cn/u/15709589\",\"fansCount\":0,\"fansCountStr\":\"0\",\"fansCountTips\":\"共0粉丝\",\"fansPageUrl\":\"http://www.test.zcool.cn/u/15709589/fans\",\"guanzhuStatus\":0,\"id\":15709589,\"memberHonors\":[],\"memberType\":0,\"pageUrl\":\"http://www.test.zcool.cn/u/15709589\",\"popularityCount\":0,\"popularityCountStr\":\"0\",\"popularityCountTips\":\"共0人气\",\"profession\":4,\"professionName\":\"网页设计师\",\"recommendTime\":1525254322362,\"status\":1,\"username\":\"把我大号抗过来\"},\"downCount\":0,\"favoriteCount\":0,\"fileId\":0,\"id\":134667,\"memo\":\"<p>大幅度放宽市场准入”“加强知识产权保护”“降低汽车进口关税”……4月10日，习近平在博鳌亚洲论坛2018年年会开幕式上宣布了中国扩大开放的一系列新举措，并再次明确宣示：中国开放的大门不会关闭，只会越开越大！<br></p>\\n<p>实打实的举措和表态，收获了现场的掌声和舆论的赞誉。然而，在中美贸易摩擦激烈的背景下，也有人疑虑：中国扩大开放的新举措是不得已而为之。</p>\\n<p>事实真是这样吗？当然不是！</p>\\n<p><strong>1.从大幅度放宽市场准入到主动扩大进口，中国早有安排</strong></p>\\n<p>对于当今世界，中国一直坚持自己的判断：经济全球化是不可逆转的时代潮流。</p>\\n<p>基于这一判断，中国这些年开放力度越来越大，具体举措不断出台。而这些都完全是在我们一贯政策主张基础上推出的，都是立足自身发展、按照自己的节奏进行的。</p>\\n<p>一个显而易见的事实是，过去40年，对外开放是中国的一项长期基本国策。根据自身情况，中国一直按照自己扩大开放的时间表和路线图循序渐进。十八大以来，中国扩大对外开放的新举措不断出台，开放步伐越来越快。十九大报告同样强调，要“坚持打开国门搞建设”。</p>\\n<p>具体到今年进一步扩大开放，同样“有章可循”。去年年底的中央经济工作会议就提出，要在开放的范围和层次上进一步拓展。</p>\\n<p>今年全国两会上，政府工作报告就提出，要扩大电信、医疗、教育、养老等领域开放，下调汽车、部分日用消费品等进口关税。甚至连特朗普近日频频亮出的知识产权问题，这份报告也早已提及：“强化知识产权保护，实行侵权惩罚性赔偿制度。”</p>\\n<p><strong>上述文件和报告白纸黑字，早已公布。中国立足自身发展需要，自行推出扩大开放新举措，但有些人非要把中国的战略抉择和当下的中美贸易摩擦背景挂钩解读，这种罔顾事实的自行脑补，可以休矣！</strong></p>\\n<p><strong>2.中国开放的大门只会越开越大，但别指望中国会在外部压力下门洞大开</strong></p>\\n<p>“我要明确告诉大家，中国开放的大门不会关闭，只会越开越大！”习近平在论坛上明确表态，再一次宣示了中国扩大开放的决心。他还同时点明：中国不以追求贸易顺差为目标，真诚希望扩大进口，促进经常项目收支平衡。</p>\\n<p>这份真诚，人们足以感受到。</p>\\n<p>目前，我国已与24个国家和地区签署了16个自由贸易协定，已生效实施15个，涵盖8000余种零关税进口产品。</p>\\n<p>自贸区协定之外，从2017年12月1日起，我国调降187项商品的进口关税，平均税率由17.3%降至7.7%。</p>\\n<p>据世贸组织统计，我国进口增速明显高于全球平均水平和美、德、日等国。而且，未来5年，中国将预计进口超过10万亿美元的商品和服务。</p>\",\"mycate\":0,\"pageUrl\":\"http://www.test.zcool.cn/article/ZNTM4NjY4.html\",\"productId\":0,\"publishTime\":1523352283000,\"publishTimeDiffStr\":\"22天前\",\"recommend\":3,\"recommendCount\":2,\"recommendCountGuess\":2,\"recommendCountStr\":\"0\",\"recommendStr\":\"首页推荐\",\"recommendTime\":1523353282000,\"sourceUrl\":\"www.baidu.com\",\"statusId\":1,\"subcate\":0,\"summary\":\"中国放宽市场准入、降低汽车关税是迫于美国压力?看事实\",\"timeTitleStr\":\"审核通过时间：2018-04-10 17:24:43；创建时间：2018-04-10 17:21:40\",\"title\":\"文章优化\",\"type\":1,\"typeStr\":\"原创\",\"updateTime\":1523353282000,\"viewCount\":113,\"viewCountStr\":\"0\",\"zteamId\":0}},\"msg\":\"success\"}";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 100; i++) {
            buffer.append(value);
        }
        value = buffer.toString();

        Timber.v("testIPC %s", key);

        StorageManager.getInstance().set(StorageManager.NAMESPACE_CACHE, key, value);

        Intent intent = new Intent(this, TaskService.class);
        intent.putExtra("read_ipc", true);
        startService(intent);
    }

}
