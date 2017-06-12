package android.jlu.com.municipalmanage.baseclass;

import java.util.List;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Task {


    private List<ProBeanBean> proBean;

    public List<ProBeanBean> getProBean() {
        return proBean;
    }

    public void setProBean(List<ProBeanBean> proBean) {
        this.proBean = proBean;
    }

    public static class ProBeanBean {


        private String address;
        private String find_pic;
        private String find_time;
        private String find_video;
        private int id;
        private double latitude;
        private double longitude;
        private int pro_state;
        private String site_desc;
        private String type;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFind_pic() {
            return find_pic;
        }

        public void setFind_pic(String find_pic) {
            this.find_pic = find_pic;
        }

        public String getFind_time() {
            return find_time;
        }

        public void setFind_time(String find_time) {
            this.find_time = find_time;
        }

        public String getFind_video() {
            return find_video;
        }

        public void setFind_video(String find_video) {
            this.find_video = find_video;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public int getPro_state() {
            return pro_state;
        }

        public void setPro_state(int pro_state) {
            this.pro_state = pro_state;
        }

        public String getSite_desc() {
            return site_desc;
        }

        public void setSite_desc(String site_desc) {
            this.site_desc = site_desc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
