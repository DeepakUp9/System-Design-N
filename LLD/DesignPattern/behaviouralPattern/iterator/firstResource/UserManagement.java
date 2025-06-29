package LLD.DesignPattern.behaviouralPattern.iterator.firstResource;

import java.util.ArrayList;

public class UserManagement{

    private ArrayList<User> userList = new ArrayList<>();


    public void addUser(User user){
        userList.add(user);
    }

    public User getUser(int idx){
        return userList.get(idx);
    }

    public MyIterator getIterator(){
        return new MyIteratorImpl(userList);
    }
}