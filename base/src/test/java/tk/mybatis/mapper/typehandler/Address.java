package tk.mybatis.mapper.typehandler;

import java.io.Serializable;

/**
 * @author liuzh
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    private String province;
    private String city;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(province != null && province.length() > 0){
            builder.append(province);
        }
        if(city != null && city.length() > 0){
            builder.append("/").append(city);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println("/hh".split("/").length);
        System.out.println("/hh".split("/")[0]);
        System.out.println("/hh".split("/")[1]);
    }
}
