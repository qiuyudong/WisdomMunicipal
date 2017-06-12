package android.jlu.com.municipalmanage.baseclass;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/20
 *     desc   :用户信息
 *     version: 1.0
 * </pre>
 */
public class User {

    /**
     * empBean : {"id":1,"name":"张杰","phone":"15693487221","title":"管理员"}
     * result : 2
     */

    private EmpBeanBean empBean;
    private int result;

    public EmpBeanBean getEmpBean() {
        return empBean;
    }

    public void setEmpBean(EmpBeanBean empBean) {
        this.empBean = empBean;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public static class EmpBeanBean {
        /**
         * id : 1
         * name : 张杰
         * phone : 15693487221
         * title : 管理员
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

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
