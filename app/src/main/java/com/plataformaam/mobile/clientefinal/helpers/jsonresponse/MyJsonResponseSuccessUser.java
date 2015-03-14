package com.plataformaam.mobile.clientefinal.helpers.jsonresponse;

import com.plataformaam.mobile.clientefinal.models.User;

import java.util.List;

/**
 * Created by bernard on 06/01/2015.
 */
public class MyJsonResponseSuccessUser extends JsonSuccessResponse {
    public class Data{
        int totalCount;
        List<User> user;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }

        public Data(int totalCount, List<User> user) {
            this.totalCount = totalCount;
            this.user = user;
        }

        public Data() {
        }

        @Override
        public String toString() {
            return "{" +
                    "totalCount=" + totalCount + '\'' +
                    ", user=" + user +
                    '}';
        }



    }


    Data data;
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public MyJsonResponseSuccessUser(String success, String message, Data data) {
        super(success, message);
        this.data = data;
    }

    public MyJsonResponseSuccessUser(Data data) {
        this.data = data;
    }

    public MyJsonResponseSuccessUser() {
    }

    @Override
    public String toString() {
        return "{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                "" +
                    "data=" + data.toString() +
                '}';
    }
}
