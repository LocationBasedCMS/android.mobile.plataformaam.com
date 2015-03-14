package com.plataformaam.mobile.clientefinal.helpers.eventbus;

import com.plataformaam.mobile.clientefinal.models.User;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;

import java.util.List;
import java.util.Map;

/**
 * Created by bernard on 02/03/2015.
 */
public class MyMessage {

    private String Sender;
    private String Message;

    private UPI upi;
    private List<VComComposite> composites;
    private Map<Integer, VComComposite> myComposites;
    private User user;
    private VComUserRole role;


    public MyMessage(String sender, String message) {
        Sender = sender;
        Message = message;
    }

    public MyMessage() {
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<VComComposite> getComposites() {
        return composites;
    }

    public void setComposites(List<VComComposite> composites) {
        this.composites = composites;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<Integer, VComComposite> getMyComposites() {
        return myComposites;
    }

    public void setMyComposites(Map<Integer, VComComposite> myComposites) {
        this.myComposites = myComposites;
    }



    public UPI getUpi() {
        return upi;
    }

    public void setUpi(UPI upi) {
        this.upi = upi;
    }

    public VComUserRole getRole() {
        return role;
    }

    public void setRole(VComUserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "{" +
                "Sender='" + Sender + '\'' +
                ", Message='" + Message + '\'' +
                ", composites=" + composites +
                ", myComposites=" + myComposites +
                ", user=" + user +
                '}';
    }
}
