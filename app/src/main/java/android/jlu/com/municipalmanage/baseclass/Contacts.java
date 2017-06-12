package android.jlu.com.municipalmanage.baseclass;

import java.util.List;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Contacts {

    private List<EmpBeanBean> empBean;

    public List<EmpBeanBean> getEmpBean() {
        return empBean;
    }

    public void setEmpBean(List<EmpBeanBean> empBean) {
        this.empBean = empBean;
    }

    public static class EmpBeanBean {
        /**
         * id : 0
         * name : 未指派
         * phone : 1
         * title : 1
         */

        private int id;
        private String name;
        private String phone;
        private String title;




        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }




        public String getTitle() {
            return title;
        }




    }
}
