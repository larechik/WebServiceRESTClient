package com.makhnovetc.rest;

import com.sun.jersey.api.client.*;
import com.sun.xml.internal.ws.client.AsyncInvoker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class App {
    private static final String URL = "http://localhost:8081/rest/persons";
    private static Client client;


    public static void main(String[] args) throws ExecutionException, InterruptedException {
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

    private static void deletePerson(BufferedReader br) throws InterruptedException, ExecutionException {
        System.out.println("Please, enter the id of the line you want deleted...");
        AsyncWebResource webResource = client.asyncResource(URL);
        String delId = null;
        try {
            delId = br.readLine();
            webResource = webResource.queryParam("id", delId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String status = null;
        Future<ClientResponse> resp =
                webResource.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
        while (!resp.isDone()) {
            Thread.sleep(1000);
        }
        ClientResponse response = resp.get();
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed: " + response.getEntity(String.class));
        }
        status = response.getEntity(String.class);
        System.out.println("status: " + status);

    }

    private static void updatePerson(BufferedReader br) throws InterruptedException, ExecutionException {
        boolean test = true;
        AsyncWebResource webResource = client.asyncResource(URL);
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
        Future<ClientResponse> resp =
                webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class);
        while (!resp.isDone()) {
            Thread.sleep(1000);
        }
        ClientResponse response = resp.get();
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed: " + response.getEntity(String.class));
        }
        status = response.getEntity(String.class);
        System.out.println("status: " + status);
    }

    private static void insertPerson(BufferedReader br) throws InterruptedException, ExecutionException {
        boolean test = true;
        AsyncWebResource webResource = client.asyncResource(URL);
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
            if (!(name.isEmpty() || middlename.isEmpty() || surname.isEmpty() || dob.isEmpty() || sex.isEmpty())) {
                test = false;
            } else {
                System.out.println("name=" + name + " middlename=" + middlename + " surname=" + surname + " dob=" + dob + " sex=" + sex);
                System.out.println("You did not specify all the options");
                test = true;
            }
        }

        Future<ClientResponse> resp =
                webResource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class);
        while (!resp.isDone()) {
            Thread.sleep(1000);
        }
        ClientResponse response = resp.get();
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed: " + response.getEntity(String.class));
        }
        //GenericType<List<Person>> type = new GenericType<List<Person>>() {};
        //GenericType<Integer> type = new GenericType<Integer>(){};
        String id = response.getEntity(String.class);
        System.out.println("id = " + response.getEntity(String.class));
    }

    private static List<Person> getAllPersons(Client client, String name) throws InterruptedException, ExecutionException {
        AsyncWebResource webResource = client.asyncResource(URL);
        if (name != null) {
            webResource = webResource.queryParam("name", name);
        }
        Future<ClientResponse> response =
                webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        while (!response.isDone()) {
            Thread.sleep(1000);
        }
        ClientResponse resp = response.get();
        if (resp.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed: " + resp.getEntity(String.class));
        }
        GenericType<List<Person>> type = new GenericType<List<Person>>() {
        };
        return resp.getEntity(type);
    }

    private static List<Person> findPersons(BufferedReader br) throws InterruptedException, ExecutionException {
        AsyncWebResource webResource = client.asyncResource(URL);
        //WebResource webResource = client.resource(URL);
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
        Future<ClientResponse> response =
                webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        //Response resp = response.
        while (!response.isDone()) {
            Thread.sleep(1000);
        }
        ClientResponse resp = response.get();
        if (resp.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed: " + resp.getEntity(String.class));
        }
        GenericType<List<Person>> type = new GenericType<List<Person>>() {
        };
        return resp.getEntity(type);
    }

    private static void printList(List<Person> persons) {
        for (Person person : persons) {
            System.out.println(person);
        }
    }
}
