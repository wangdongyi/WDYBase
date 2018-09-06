package com.wdy.base.module.view.adresse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wepon on 2017/12/4.
 * 数据模型
 */

public class YwpAddressBean implements Serializable {


    @SerializedName("provinces")
    private List<ProvincesBean> provinces;

    public List<ProvincesBean> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvincesBean> provinces) {
        this.provinces = provinces;
    }

    public static class ProvincesBean {
        /**
         * cities : [{"counties":[{"id":"110101","name":"东城区"},{"id":"110102","name":"西城区"},{"id":"110105","name":"朝阳区"},{"id":"110106","name":"丰台区"},{"id":"110107","name":"石景山区"},{"id":"110108","name":"海淀区"},{"id":"110109","name":"门头沟区"},{"id":"110111","name":"房山区"},{"id":"110112","name":"通州区"},{"id":"110113","name":"顺义区"},{"id":"110114","name":"昌平区"},{"id":"110115","name":"大兴区"},{"id":"110116","name":"怀柔区"},{"id":"110117","name":"平谷区"},{"id":"110118","name":"密云区"},{"id":"110119","name":"延庆区"}],"id":"110100","name":"北京市"}]
         * id : 110000
         * name : 北京市
         */

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("cities")
        private List<CitiesBean> cities;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CitiesBean> getCities() {
            return cities;
        }

        public void setCities(List<CitiesBean> cities) {
            this.cities = cities;
        }

        public static class CitiesBean {
            /**
             * counties : [{"id":"110101","name":"东城区"},{"id":"110102","name":"西城区"},{"id":"110105","name":"朝阳区"},{"id":"110106","name":"丰台区"},{"id":"110107","name":"石景山区"},{"id":"110108","name":"海淀区"},{"id":"110109","name":"门头沟区"},{"id":"110111","name":"房山区"},{"id":"110112","name":"通州区"},{"id":"110113","name":"顺义区"},{"id":"110114","name":"昌平区"},{"id":"110115","name":"大兴区"},{"id":"110116","name":"怀柔区"},{"id":"110117","name":"平谷区"},{"id":"110118","name":"密云区"},{"id":"110119","name":"延庆区"}]
             * id : 110100
             * name : 北京市
             */

            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("counties")
            private List<CountiesBean> counties;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<CountiesBean> getCounties() {
                return counties;
            }

            public void setCounties(List<CountiesBean> counties) {
                this.counties = counties;
            }

            public static class CountiesBean {
                /**
                 * id : 110101
                 * name : 东城区
                 */

                @SerializedName("id")
                private String id;
                @SerializedName("name")
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
