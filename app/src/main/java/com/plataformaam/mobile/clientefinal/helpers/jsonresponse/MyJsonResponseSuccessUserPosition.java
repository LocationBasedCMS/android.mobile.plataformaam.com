package com.plataformaam.mobile.clientefinal.helpers.jsonresponse;

import com.plataformaam.mobile.clientefinal.models.location.UserPosition;

import java.util.List;

/**
 * Created by bernard on 28/01/2015.
 */
public class MyJsonResponseSuccessUserPosition extends JsonSuccessResponse  {
    public class Data{
        int totalCount;
        List<UserPosition> userposiotion;


        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<UserPosition> getUserposiotion() {
            return userposiotion;
        }

        public void setUserposiotion(List<UserPosition> userposiotion) {
            this.userposiotion = userposiotion;
        }

        public Data(int totalCount, List<UserPosition> userposiotion) {
            this.totalCount = totalCount;
            this.userposiotion = userposiotion;
        }

        @Override
        public String toString() {
            return "{" +
                    "totalCount=" + totalCount +
                    ", userposiotion=" + userposiotion +
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

    public MyJsonResponseSuccessUserPosition(String success, String message, Data data) {
        super(success, message);
        this.data = data;
    }

    public MyJsonResponseSuccessUserPosition(Data data) {
        this.data = data;
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
