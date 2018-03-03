package com.example.csl.aixiaocai.enity;

import java.util.List;

/**
 * 识别的返回
 * Created by csl on 2018/3/2.
 */

public class BaiduEnity {


    /**
     * merged_res : {"semantic_form":{"appid":19361,"err_no":0,"parsed_text":"e 绅士 不 了 ， ","raw_text":"e绅士不了，","results":[]}}
     */

    private MergedResBean merged_res;

    public MergedResBean getMerged_res() {
        return merged_res;
    }

    public void setMerged_res(MergedResBean merged_res) {
        this.merged_res = merged_res;
    }

    public static class MergedResBean {
        /**
         * semantic_form : {"appid":19361,"err_no":0,"parsed_text":"e 绅士 不 了 ， ","raw_text":"e绅士不了，","results":[]}
         */

        private SemanticFormBean semantic_form;

        public SemanticFormBean getSemantic_form() {
            return semantic_form;
        }

        public void setSemantic_form(SemanticFormBean semantic_form) {
            this.semantic_form = semantic_form;
        }

        public static class SemanticFormBean {
            /**
             * appid : 19361
             * err_no : 0
             * parsed_text : e 绅士 不 了 ，
             * raw_text : e绅士不了，
             * results : []
             */

            private int appid;
            private int err_no;
            private String parsed_text;
            private String raw_text;
            private List<?> results;

            public int getAppid() {
                return appid;
            }

            public void setAppid(int appid) {
                this.appid = appid;
            }

            public int getErr_no() {
                return err_no;
            }

            public void setErr_no(int err_no) {
                this.err_no = err_no;
            }

            public String getParsed_text() {
                return parsed_text;
            }

            public void setParsed_text(String parsed_text) {
                this.parsed_text = parsed_text;
            }

            public String getRaw_text() {
                return raw_text;
            }

            public void setRaw_text(String raw_text) {
                this.raw_text = raw_text;
            }

            public List<?> getResults() {
                return results;
            }

            public void setResults(List<?> results) {
                this.results = results;
            }
        }
    }
}
