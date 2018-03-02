package com.example.csl.aixiaocai.enity;

import java.util.List;

/**
 * 识别的返回
 * Created by csl on 2018/3/2.
 */

public class BaiduEnity {

    /**
     * result_type : final_result
     * best_result : 谢谢，
     * results_recognition : ["谢谢，"]
     * origin_result : {"result":{"word":["谢谢，"]},"sn":"12554d65-0c4d-4c2c-b32a-094ccfa428bd_s-0","err_no":0,"corpus_no":6528250973597516449}
     * error : 0
     */

    private String result_type;
    private String best_result;
    private OriginResultBean origin_result;
    private int error;
    private List<String> results_recognition;

    public String getResult_type() {
        return result_type;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }

    public String getBest_result() {
        return best_result;
    }

    public void setBest_result(String best_result) {
        this.best_result = best_result;
    }

    public OriginResultBean getOrigin_result() {
        return origin_result;
    }

    public void setOrigin_result(OriginResultBean origin_result) {
        this.origin_result = origin_result;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<String> getResults_recognition() {
        return results_recognition;
    }

    public void setResults_recognition(List<String> results_recognition) {
        this.results_recognition = results_recognition;
    }

    public static class OriginResultBean {
        /**
         * result : {"word":["谢谢，"]}
         * sn : 12554d65-0c4d-4c2c-b32a-094ccfa428bd_s-0
         * err_no : 0
         * corpus_no : 6528250973597516449
         */

        private ResultBean result;
        private String sn;
        private int err_no;
        private long corpus_no;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getErr_no() {
            return err_no;
        }

        public void setErr_no(int err_no) {
            this.err_no = err_no;
        }

        public long getCorpus_no() {
            return corpus_no;
        }

        public void setCorpus_no(long corpus_no) {
            this.corpus_no = corpus_no;
        }

        public static class ResultBean {
            private List<String> word;

            public List<String> getWord() {
                return word;
            }

            public void setWord(List<String> word) {
                this.word = word;
            }
        }
    }
}
