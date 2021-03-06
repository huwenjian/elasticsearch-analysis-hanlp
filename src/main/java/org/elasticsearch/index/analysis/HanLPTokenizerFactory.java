package org.elasticsearch.index.analysis;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.utility.Predefine;
import com.hankcs.lucene4.HanLPTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import java.util.List;

/**
 */
public class HanLPTokenizerFactory extends AbstractTokenizerFactory {

    private boolean enablePorterStemming;
    private boolean enableIndexMode;
    private static String sysPath = String.valueOf(System.getProperties().get("user.dir")) ;

    public HanLPTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        Predefine.HANLP_PROPERTIES_PATH = sysPath.substring(0, sysPath.length()-4) + "/plugins/analysis-hanlp/hanlp.properties";
        enablePorterStemming = settings.getAsBoolean("enablePorterStemming", false);
    }

    public static HanLPTokenizerFactory getIndexTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings).setIndexMode(true);
    }

    public static HanLPTokenizerFactory getSmartTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings).setIndexMode(false);
    }

    private HanLPTokenizerFactory setIndexMode(boolean enableIndexMode) {
        this.enableIndexMode = enableIndexMode;
        return this;
    }

    @Override
    public Tokenizer create() {
        return new HanLPTokenizer(HanLP.newSegment().enablePlaceRecognize(true).enableCustomDictionary(true).enableIndexMode(enableIndexMode).enableOffset(true), null, enablePorterStemming);
    }

}

