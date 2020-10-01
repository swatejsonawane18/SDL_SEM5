import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import java.sql.*;

class Server extends Thread {

    static void add_to_database(String usertype,String user,String name,String email,String phone,String password,String location) throws Exception{
       Class.forName("com.mysql.cj.jdbc.Driver");
       Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmarket_schema","root","$w@teJ1800");
       Statement st= con.createStatement();
       String query="insert into server_database values(null,'"+usertype+"','"+user+"','"+password+"','"+name+"','"+email+"','"+phone+"','"+location+"');";
        st.executeUpdate(query);
   }

   public static  String getname(int id){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmarket_schema", "root", "$w@teJ1800");
            Statement st = con.createStatement();
            String query = "select name from server_database where id=" + id + ";";
            ResultSet rs = st.executeQuery(query);
            rs.next();
            return (rs.getString(1));
        }
        catch (Exception e){
            System.out.println(e);
            return "null";
        }
   }

    public static void main(String argv[]) throws Exception {
        String user,password,query,address,email,phone;
        int  clientlogintype,ch;
        //String capitalizedSentence;
        //Scanner scan =new Scanner(System.in);
        HashMap<String,Customer> customer=new HashMap<>();
        HashMap<String,Shops> shop=new HashMap<>();

        ServerSocket welcomeSocket = new ServerSocket(5000);

        String url="jdbc:mysql://localhost:3306/farmarket_schema";
        String name="root";
        String pass="$w@teJ1800";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url,name,pass);
        Statement st= con.createStatement();
        ResultSet rs;


        while (true) {
            System.out.println("ServerSocket awaiting connections...");
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Connection from " + connectionSocket);

            DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            while (true) {
                System.out.println("****** WELCOME TO AGRICULTURE-MARKET MANAGEMENTS ********");
                System.out.println();
                System.out.println("1.Login");
                System.out.println("2.Register");
                System.out.println("3.Exit");
                clientlogintype = inFromClient.readInt();

                if(clientlogintype==1){ //Login

                    System.out.println("Choose User Type (INTEGER)- ");
                    System.out.println("1.Customer");
                    System.out.println("2.Shop Member");
                    ch=inFromClient.readInt();

                    if(ch==1){ //Customer Login
                        System.out.println("Enter Username");
                        user=inFromClient.readUTF();
                        query="SELECT username FROM server_database WHERE username='"+user+"' and usertype='Customer';";
                        rs = st.executeQuery(query);
                        if(rs.next()){
                            Customer obj2=new Customer();
                            outToClient.writeInt(1);
                            System.out.println("Enter Password");
                            password=inFromClient.readUTF();
                            query="SELECT id,password FROM server_database WHERE username='"+user+"'and usertype='Customer';";
                            rs = st.executeQuery(query);
                            rs.next();

                            if((rs.getString(2)).equals(password)){
                                outToClient.writeInt(1);
                                obj2.main(rs.getInt(1));
                                continue;
                            }
                            else{
                                outToClient.writeInt(0);
                                System.out.println("Invalid Password!");
                            }
                        }
                        else{
                            outToClient.writeInt(0);
                            System.out.println("User not present!!");
                        }
                    }

                    else if(ch==2){ //Shops Login
                        System.out.println("Enter Username");
                        user=inFromClient.readUTF();

                        query="SELECT username FROM server_database WHERE username='"+user+"' and usertype='Shop';";
                        rs = st.executeQuery(query);
                        if(rs.next()){
                            Shops obj=new Shops();
                            outToClient.writeInt(1);
                            System.out.println("Enter Password");
                            password=inFromClient.readUTF();
                            query="SELECT id,password FROM server_database WHERE username='"+user+"'and usertype='Shop';";
                            rs = st.executeQuery(query);
                            rs.next();

                            if((rs.getString(2)).equals(password)){
                                outToClient.writeInt(1);
                                obj.main1(rs.getInt(1));
                                continue;
                            }
                            else{
                                outToClient.writeInt(0);
                                System.out.println("Invalid Password!");
                            }
                        }
                        else{
                            outToClient.writeInt(0);
                            System.out.println("User not present!!");
                        }
                    }

                }
                if (clientlogintype==2) {  //Register

                    System.out.println("Choose User Type - ");
                    System.out.println("1.Customer");
                    System.out.println("2.Shop Member");
                    ch = inFromClient.readInt();

                    if(ch==1){
                        while(true){
                            System.out.println("Enter Username -");
                            user=inFromClient.readUTF();
                            query="SELECT username FROM server_database WHERE username='"+user+"';";
                            rs = st.executeQuery(query);
                            if(rs.next()){
                                System.out.println("Username already choosen! Try different Username!");
                                outToClient.writeInt(0);
                            }
                            else {
                                outToClient.writeInt(1);
                                break;
                            }
                       }

                        System.out.println("Enter Customer Name -");
                        String customer_name=inFromClient.readUTF();
                        System.out.println(customer_name);
                        System.out.println("Enter Email Id -");
                        email=inFromClient.readUTF();
                        System.out.println(email);
                        System.out.println("Enter Phone Number - ");
                        phone=inFromClient.readUTF();
                        System.out.println(phone);
                        System.out.println("Enter Address -");
                        address=inFromClient.readUTF();
                        System.out.println(address);
                        System.out.println("Enter Password -");
                        password=inFromClient.readUTF();
                        System.out.println(password);

                        add_to_database("Customer",user,customer_name,email,phone,password,address);
                        System.out.println();
                        System.out.println("You are ready to go! Please Log in. ");
                    }

                    else if(ch==2){
                        while (true) {
                            System.out.println("Enter Username -");
                            user = inFromClient.readUTF();
                            System.out.println(user);
                            query="SELECT username FROM server_database WHERE username='"+user+"';";
                            rs = st.executeQuery(query);
                            if(rs.next()){
                                System.out.println("Username already choosen! Try different Username!");
                                outToClient.writeInt(0);
                            }
                            else {
                                outToClient.writeInt(1);
                                break;
                            }
                        }

                        System.out.println("Enter Shop Name -");
                        String shop_name=inFromClient.readUTF();
                        System.out.println(shop_name);
                        System.out.println("Enter Email Id -");
                        email=inFromClient.readUTF();
                        System.out.println(email);
                        System.out.println("Enter Phone Number - ");
                        phone=inFromClient.readUTF();
                        System.out.println(phone);
                        System.out.println("Enter Address -");
                        address=inFromClient.readUTF();
                        System.out.println(address);
                        System.out.println("Enter Password -");
                        password=inFromClient.readUTF();
                        System.out.println(password);

                        add_to_database("Shop",user,shop_name,email,phone,password,address);

                        System.out.println("You are ready to go! Please Log in. ");
                    }
                }
                else if (clientlogintype==3) {
                    outToClient.writeUTF("Thank You!");
                    break;
                }
            }

        }
    }
}

