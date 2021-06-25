package com.geral.main;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Main {

    //Como o id é de 2 digitos, os DDIs variam de 01 a 99.
    static int qtdMaxDDI = 100;
    static int[] qtdPerDDI = new int[qtdMaxDDI];
    static Map<String, String> mapStatusID = new TreeMap<>();
    static int[] assinadosPorPais = new int[qtdMaxDDI];
    static File file;


    public static void main(String[] args) throws IOException {

        Locale.setDefault(new Locale("pt","BR"));

        String path;
        if (args[0].equals("path")) {
            path = args[1];
        } else {
            path = getPath();
        }

        putFileDataToArrays(path);
        exportFormattedToFile(path);
    }

    public static String getPath() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o endereço do arquivo(caso esteja na mesma pasta do programa, digite apenas o nome do arquivo)");
        String path = sc.next();
        sc.close();

        return path;
    }

    public static void putFileDataToArrays(String filePath) throws IOException {
        file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        //Pega o DDI, ID e status(assinado ou cancelado) por linha e armazena nas listas
        String line;
        while (( line = br.readLine()) != null) {
            Integer digits = Integer.parseInt(line.substring(0,2));
            String idUser = "";

            int i = 0;
            char c = line.charAt(i);
            while (Character.isDigit(c)) {
                idUser += c;
                c = line.charAt(++i);
            }

            String status = line.substring(i+1);
            if (!mapStatusID.containsKey(idUser)) {
                qtdPerDDI[digits] = qtdPerDDI[digits] + 1;
            }
            mapStatusID.put(idUser, status);
        }

        br.close();

        //Conta a quantidade de assinados por país.
        //Só pode ser feita depois de ler o arquivo e retirar os duplicados.
        for (Map.Entry<String, String> value : mapStatusID.entrySet()) {
            int DDI = Integer.parseInt(value.getKey().substring(0,2));
            if (value.getValue().equals("assinado")) {
                assinadosPorPais[DDI] = assinadosPorPais[DDI] + 1;
            }
        }
    }

    public static void exportFormattedToFile(String path) throws IOException {
        String fileName = "out-" + file.getName();
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

        int qtdPerCountry;
        int assignedPerCountry;
        for (int i=0; i<qtdMaxDDI; i++) {
            Integer DDI = i;
            if (qtdPerDDI[DDI] > 0) {
                String country = getCountryName(DDI);

                // Exceções:
                //  Cazaquistão - tem 2 DDIs: 76 e 77
                if (i==76) {
                    qtdPerCountry = qtdPerDDI[i] + qtdPerDDI[i+1];
                    assignedPerCountry = assinadosPorPais[i] + assinadosPorPais[i+1];
                    i++;
                } else {
                    qtdPerCountry = qtdPerDDI[i];
                    assignedPerCountry = assinadosPorPais[i];
                }

                bw.write(country + "," + qtdPerCountry + "," + assignedPerCountry);
                bw.newLine();
            }
        }
        bw.close();
    }

    public static String getCountryName(Integer countryCode) {
        //Exceções:
        if (countryCode==1) { return "Estados Unidos / Canada"; }

        String urlString = "https://restcountries.eu/rest/v2/callingcode/";
        String filter = "?fields=translations";

        //Requisição GET ao site restcountries.eu
        try {
            URL url = new URL(urlString + countryCode + filter);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Erro: " + conn.getResponseCode());
            }
            InputStreamReader iS = new InputStreamReader(conn.getInputStream());
            BufferedReader bR = new BufferedReader(iS);

            String output = bR.readLine();
            //Pega, dentro do JSON recebido, a tradução para o brasil.
            int first = output.lastIndexOf("\"br\"") + 6;
            int last = first;
            while (output.charAt(last) != '"') {
                last++;
            }

            output = output.substring(first,last);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return "País inexistente";
        }


    }
}

