package com.makhnovetc.rest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import javax.ws.rs.core.MediaType;
public class App {
    private static final String URL = "http://localhost:8081/rest/persons";
    private static Client client;
    public static void main(String[] args) {
        client = Client.create();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Select the type of action: 1 - select; 2 - insert; 3 - update; 4 - delete");
        String actionType = null;
        try {
            actionType = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (actionType) {
            case "1":
                findPersons(br);
                break;
            case "2":
                insertPerson(br);
                break;
            case "3":
                updatePerson(br);
                break;
            case "4":
                deletePerson(br);
                break;
            default:
                System.out.println("Wrong choose");
                break;
        }
    }

    private static void deletePerson(BufferedReader br) {
        System.out.println("Please, enter the id of the line you want deleted...");
        WebResource webResource = client.resource(URL);
        String delId = null;
        try {
            delId = br.readLine();
            webResource = webResource.queryParam("id", delId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String status = null;
        ClientResponse response =
                webResource.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
        status = response.getEntity(String.class);
        System.out.println("status: " + response.getEntity(String.class));

    }

    private static void updatePerson(BufferedReader br) {
        boolean test = true;
        WebResource webResource = client.resource(URL);
        while (test) {
            System.out.println("Please, enter new fields values and the id of the line you want...");
            System.out.println("id-for column id");
            System.out.println("fn-for column name");
            System.out.println("mn-for column middle_name");
            System.out.println("sr-for column surname");
            System.out.println("dob-for column dob");
            System.out.println("sex-for column sex");
            System.out.println("Example:");
            System.out.println("fn=Alexandr");
            System.out.println("Enter your request with delim \",\"");
            String readreq = null;
            try {
                readreq = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String req[] = readreq.split(",");
            String id = "", name = "", surname = "", middlename = "", dob = "", sex = "";
            for (int j = 0; j < req.length; j++) {
                String temp[] = req[j].split("=");
                switch (temp[0]) {
                    case "id":
                        id = temp[1];
                        webResource = webResource.queryParam("id", id);
                        break;
                    case "fn":
                        name = temp[1];
                        webResource = webResource.queryParam("name", name);
                        break;
                    case "mn":
                        middlename = temp[1];
                        webResource = webResource.queryParam("middlename", middlename);
                        break;
                    case "sr":
                        surname = temp[1];
                        webResource = webResource.queryParam("surname", surname);
                        break;
                    case "dob":
                        dob = temp[1];
                        webResource = webResource.queryParam("dob", dob);
                        break;
                    case "sex":
                        sex = temp[1];
                        webResource = webResource.queryParam("sex", sex);
                        break;
                }
            }
            if (!(id.isEmpty() || name.isEmpty() || middlename.isEmpty() || surname.isEmpty() || dob.isEmpty() || sex.isEmpty())) {
                test = false;
            } else {
                System.out.println("name=" + name + " middlename=" + middlename + " surname=" + surname + " dob=" + dob + " sex=" + sex);
                System.out.println("You did not specify all the options");
            }

        }
        String status = null;
        ClientResponse response =
                webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class);
        status = response.getEntity(String.class);
        System.out.println("status: " + response.getEntity(String.class));
    }

    private static void insertPerson(BufferedReader br) {
        boolean test = true;
        WebResource webResource = client.resource(URL);
        while (test) {
            System.out.println("Please, write your query...");
            System.out.println("fn-for column name");
            System.out.println("mn-for column middle_name");
            System.out.println("sr-for column surname");
            System.out.println("dob-for column dob");
            System.out.println("sex-for column sex");
            System.out.println("Example:");
            System.out.println("fn=Alexandr");
            System.out.println("Enter your request with delim \",\"");
            String readreq = null;
            try {
                readreq = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String req[] = readreq.split(",");
            String name = "", surname = "", middlename = "", dob = "", sex = "";
            for (int j = 0; j < req.length; j++) {
                String temp[] = req[j].split("=");
                if ("fn".equals(temp[0])) {
                    name = temp[1];
                    webResource = webResource.queryParam("name", name);
                } else if ("mn".equals(temp[0])) {
                    middlename = temp[1];
                    webResource = webResource.queryParam("middlename", middlename);
                } else if ("sr".equals(temp[0])) {
                    surname = temp[1];
                    webResource = webResource.queryParam("surname", surname);
                } else if ("dob".equals(temp[0])) {
                    dob = temp[1];
                    webResource = webResource.queryParam("dob", dob);
                } else if ("sex".equals(temp[0])) {
                    sex = temp[1];
                    webResource = webResource.queryParam("sex", sex);
                }
            }
            if (!(name.isEmpty()||middlename.isEmpty()||surname.isEmpty()||dob.isEmpty()||sex.isEmpty())){
                test = false;
            }else {
                System.out.println("name="+name+" middlename="+middlename+" surname="+surname+" dob="+dob+" sex="+sex);
                System.out.println("You did not specify all the options");
                test = true;
            }
        }

        ClientResponse response =
                webResource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class);

        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed");
        }
        //GenericType<List<Person>> type = new GenericType<List<Person>>() {};
        //GenericType<Integer> type = new GenericType<Integer>(){};
        String id = response.getEntity(String.class);
        System.out.println("id = " + response.getEntity(String.class));
    }

    private static List<Person> getAllPersons(Client client, String name) {
        WebResource webResource = client.resource(URL);
        if (name != null) {
            webResource = webResource.queryParam("name", name);
        }
        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed");
        }
        GenericType<List<Person>> type = new GenericType<List<Person>>() {};
        return response.getEntity(type);
    }

    private static List<Person> findPersons(BufferedReader br) {
        WebResource webResource = client.resource(URL);
        System.out.println("Please, write your query...");
        System.out.println("id-for column id");
        System.out.println("fn-for column name");
        System.out.println("mn-for column middle_name");
        System.out.println("sr-for column surname");
        System.out.println("dob-for column dob");
        System.out.println("sex-for column sex");
        System.out.println("Example:");
        System.out.println("fn=Alexandr");
        System.out.println("Enter your request with delim \",\"");
        String readreq = null;
        try {
            readreq = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String req[] = readreq.split(",");
        String id = "", name = "", surname = "", middlename = "", dob = "", sex = "";
        for (int j = 0; j < req.length; j++) {
            String temp[] = req[j].split("=");
            if ("id".equals(temp[0])) {
                id = temp[1];
                webResource = webResource.queryParam("id", id);
            } else if ("fn".equals(temp[0])) {
                name = temp[1];
                webResource = webResource.queryParam("fn", name);
            } else if ("mn".equals(temp[0])) {
                middlename = temp[1];
                webResource = webResource.queryParam("mn", middlename);
            } else if ("sr".equals(temp[0])) {
                surname = temp[1];
                webResource = webResource.queryParam("sr", surname);
            } else if ("dob".equals(temp[0])) {
                dob = temp[1];
                webResource = webResource.queryParam("dob", dob);
            } else if ("sex".equals(temp[0])) {
                sex = temp[1];
                webResource = webResource.queryParam("sex", sex);
            }
        }

        List<Person> persons = null;
        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed");
        }
        GenericType<List<Person>> type = new GenericType<List<Person>>() {};
        return response.getEntity(type);
    }
    private static void printList(List<Person> persons) {
        for (Person person : persons) {
            System.out.println(person);
        }
    }
}