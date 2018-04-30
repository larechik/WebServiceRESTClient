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
        printList(findPersons(br));
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