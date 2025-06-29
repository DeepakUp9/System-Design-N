Solid Principles 

1. Single Responsibility Principle (SRP) => the class have only one reason to change 
   - invoice
2. Open/Closed Principle (OCP) => open for extenstion but closed for modification
    - error logger(debug error)
3. Liskov Substitution Principle (LSP) => whevere parent reference used we can replace with child object wihout breaking the code 
    - User(Customer,Delivery Patrnear, Resturnet partnear)
    - Reactangle(Suqare)
    - Ui Widget(click-> text, button)
4.Interface Segregation Principle (ISP) => small interface, don't focrce the class to implememt withoud needed
  - User(Customer,Delivery Patrnear, Resturnet partnear)
5.  Dependency Inversion Principle (DIP) => low module don't directly depened on high level module 
   - Notication(WA noti, Sms Noti)
