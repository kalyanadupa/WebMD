/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kalyan
 */
public class WebMD_URL_Users {

    /**
     * @param args
     * @throws IOException
     *
     */
    public static void main(String[] args) throws IOException {

        int count = 0;
        HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
        HashMap<String, Integer> hashmap1 = new HashMap<String, Integer>();
        HashMap<String, Integer> userMap = new HashMap<String, Integer>();
        HashMap<String, Integer> urlUserMap = new HashMap<String, Integer>();
        HashMap<String, Integer> helProMap = new HashMap<String, Integer>();
        HashMap<String, Integer> staffMap = new HashMap<String, Integer>();
        List<String> uniqueSet = new ArrayList<String>();
        Pattern p8 = Pattern.compile(".{1,50}www.{1,500}");
        String[] fileNames = {"webmd_addiction", "webmd_adhd", "webmd_breast_cancer", "webmd_diabetes", "webmd_diet", "webmd_fkids", "webmd_heart", "webmd_ms", "webmd_pain", "webmd_sexualhealth"};
//        String[] fileNames = {"webmd_fkids"};
        //*******************************For the matchings of staff and qid**************************/////

        for (String fileLog : fileNames) {
            int HelPro = 0;
            int staffCount = 0;
            int q1Post = 0;
            int q2Post = 0;
            int q3Post = 0;
            int staffPost = 0;
            int helProPost = 0;
            int urlUserCount = 0;
            hashmap = new HashMap<String, Integer>();
            hashmap1 = new HashMap<String, Integer>();
            userMap = new HashMap<String, Integer>();
            helProMap = new HashMap<String, Integer>();
            staffMap = new HashMap<String, Integer>();
            uniqueSet = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader("data/" + fileLog + ".csv"));
            urlUserMap = new HashMap<String, Integer>();
            BufferedWriter writer = new BufferedWriter(new FileWriter("test/" + fileLog + "_PU.txt"));


            while (br.ready()) {

                String check = br.readLine();
                String[] tokenizedTerms1 = check.split("\"");
                //String nameToken1 = tokenizedTerms1[3];
                String[] tokenizedTerms = check.split(",");
                String token = tokenizedTerms[1];
                String nameToken = tokenizedTerms1[3];

                if((nameToken.length() > 3) && (nameToken.charAt(nameToken.length() - 1) == ' ')){
                    nameToken = nameToken.substring(0, nameToken.length() - 1);
                }                
                //if (check.contains("http") || check.contains("www")) {
                    if (nameToken.toUpperCase().matches(".*\\,\\s*MSN") || nameToken.toUpperCase().matches(".*\\,\\s*RNP") || nameToken.toUpperCase().matches(".*\\,\\s*CDOE") || nameToken.toUpperCase().matches(".*\\,\\s*MD") || nameToken.toUpperCase().matches(".*\\,\\s*MPH") || nameToken.toUpperCase().matches(".*\\,\\s*PHD") || nameToken.toUpperCase().matches(".*\\,\\s*PT") || nameToken.toUpperCase().matches(".*\\,\\s*DSc") || nameToken.toUpperCase().matches(".*\\,\\s*NCS") || nameToken.toUpperCase().matches(".*\\,\\s*MSCS") || nameToken.toUpperCase().matches(".*\\,\\s*RN") || nameToken.toUpperCase().matches("\\w+\\_MSN") || nameToken.toUpperCase().matches("\\w+\\_RNP") || nameToken.toUpperCase().matches("\\w+\\_CDOE") || nameToken.toUpperCase().matches("\\w+\\_MD") || nameToken.toUpperCase().matches("\\w+\\_MPH") || nameToken.toUpperCase().matches("\\w+\\_PHD") || nameToken.toUpperCase().matches("\\w+\\_PT") || nameToken.toUpperCase().matches("\\w+\\_DSc") || nameToken.toUpperCase().matches("\\w+\\_NCS") || nameToken.toUpperCase().matches("\\w+\\_MSCS") || nameToken.toUpperCase().matches("\\w+\\_RN")) {
                        helProPost++;
                        if (helProMap.get(nameToken) == null) {
                            helProMap.put(nameToken, 1);
                        } else {
                            helProMap.put(nameToken, helProMap.get(nameToken) + 1);
                        }
                        continue;
                    }
                    if (nameToken.contains("WebMD_Staff")) {
                        staffPost++;

                        if (staffMap.get(nameToken) == null) {
                            staffMap.put(nameToken, 1);
                            
                        } else {
                            staffMap.put(nameToken, staffMap.get(nameToken) + 1);
                        }
                        continue;
                    }

                    if (userMap.get(nameToken) == null) {
                        userMap.put(nameToken, 1);
                        if(check.contains("http") || check.contains("www"))
                                urlUserCount ++;
                    } else {
                        userMap.put(nameToken, userMap.get(nameToken) + 1);
                        if(check.contains("http") || check.contains("www"))
                                urlUserCount ++;
                    }
                    
                //}
            }
            br.close();
            //System.out.println("Total Size = "+ userMap.size() +"\n staff Size"+ staffMap.size() +"\n helPro"+ helProMap.size());

//            ValueComparator<String, Integer> comparator = new ValueComparator<String, Integer>(userMap);
//            Map<String, Integer> sortedUserMap = new TreeMap<String, Integer>(comparator);
//            sortedUserMap.putAll(userMap);
//
//            List<String> sortedList = new ArrayList<String>(sortedUserMap.keySet());
            //System.out.println(sortedUserMap);
//            System.out.println(sortedList);
            SortedSet<Map.Entry<String, Integer>> sortedUserMap = entriesSortedByValues(userMap);
            //System.out.println(sortedUserMap);
            br = new BufferedReader(new FileReader("data/" + fileLog + ".csv"));
            int userSize = sortedUserMap.size();
            //System.out.println("sorted size: " + sortedUserMap.size() +" original size"+userMap.size() );
            int top25 = userSize / 4;
            int mid50 = userSize - top25 * 2;
            HelPro = helProMap.size();
            staffCount = staffMap.size();
            staffCount = staffMap.size();
            System.out.println("**"+ fileLog+" Log **");
            System.out.println("Q1 Users: "+ top25);
            System.out.println("Q2~Q3 Users: "+ mid50);
            System.out.println("Q4 User: "+ top25);
            System.out.println("All users :" + userMap.size());
            mid50 = userSize - top25;
            List<String> sortedUserList = new ArrayList<String>();
            for (Map.Entry<String, Integer> ent : sortedUserMap) {
                sortedUserList.add(ent.getKey());
            }
            

//            for (Map.Entry<String, Integer> entry : sortedUserMap.entrySet()) {
//                int selectedUserIndex = sortedList.indexOf(entry.getKey());
//                if(selectedUserIndex <= top25)
//                    System.out.print("First Class \t");
//                else if((selectedUserIndex <= mid50)&&(selectedUserIndex > top25))
//                    System.out.print("Second Class \t");
//                else
//                    System.out.print("Third Class \t");
//                System.out.println(entry.getKey() +"\t"+ entry.getValue());
//            }
            q1Post = q2Post = q3Post = 0;
            while (br.ready()) {
                String check = br.readLine();
                String[] tokenizedTerms1 = check.split("\"");
                //String nameToken1 = tokenizedTerms1[3];
                String[] tokenizedTerms = check.split(",");
                String token = tokenizedTerms[1];
                String nameToken = tokenizedTerms1[3];
                if (nameToken.length() < 3) {
                    continue;
                }
                
                if (nameToken.charAt(nameToken.length() - 1) == ' ') {
                    nameToken = nameToken.substring(0, nameToken.length() - 1);
                }
                int selectedUserIndex = sortedUserList.indexOf(nameToken);
                /*
                set the condition of the if loop to:
                For 1st Quartile: (selectedUserIndex < mid50)&&(selectedUserIndex <= top25)&&(selectedUserIndex != -1)
                For 2nd+3rd Quartile: (selectedUserIndex <= mid50)&&(selectedUserIndex > top25)&&(selectedUserIndex != -1)
                For 4th Quartile: (selectedUserIndex > mid50)&&(selectedUserIndex > top25)&&(selectedUserIndex != -1)
                For Health professionals: helProMap.containsKey(nameToken)
                For WebMD_Staff : staffMap.containsKey(nameToken)
                */
                if((selectedUserIndex <= mid50)&&(selectedUserIndex < top25)&&(selectedUserIndex != -1)){
//                if (helProMap.containsKey(nameToken)) {
                    if ((check.contains("www")) | (check.contains("http"))) {
                        if (urlUserMap.get(nameToken) == null) {
                            urlUserMap.put(nameToken, 1);

                        } else {
                            urlUserMap.put(nameToken, urlUserMap.get(nameToken) + 1);
                        }
                        String check2 = " ";
                        String check3 = " ";
                        Matcher m8 = p8.matcher(check);
                        List<String> matchstring8 = new ArrayList<String>();

                        while (m8.find()) {
                            check2 = check2 + "" + m8.group();
                            matchstring8.add(m8.group());
//                    System.out.println(m8.group());
                        }

                        Pattern p9 = Pattern.compile("https?\\:\\/\\/[\\-w\\.]*(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]|www\\.(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]");
                        Matcher m9 = p9.matcher(check2);
                        List<String> matchstring9 = new ArrayList<String>();
                        while (m9.find()) {
                            check3 = check3 + "" + m9.group();
                            matchstring9.add(m9.group());
//                      System.out.println(m9.group());
                        }

                        Pattern p10 = Pattern.compile("www.*?(\\.com(?=\\W)|\\.com\\,?\\)?|\\.org(?=\\W)|\\.ORG(?=\\W)|\\.net(?=\\W)|\\.gov(?=\\W)|\\.co.uk|\\.html|\\.htm|\\.asp|\\.aspx|\\.edu|\\.us|treatment|\\-men(?=\\W)|\\.pdf|\\.ca\\/servlet|\\.ch(?=\\W)|\\.coream(?=\\W)|\\.ee(?=\\W))|www.*?(?=\\/)|(?<=http\\:\\/\\/).*?(?=\\/)|(?<=https\\:\\/\\/).*?(?=\\/)");
                        Matcher m10 = p10.matcher(check3);
                        List<String> matchstring10 = new ArrayList<String>();

                        while (m10.find()) {
                            matchstring10.add(m10.group());
//				  System.out.println(m10.group());
                        }
                        //*******************************Unique set created and items put in HashMap (hashMap)**************************/////
                        for (int i = 0; i < matchstring10.size(); i++) {
                            if (uniqueSet.contains(matchstring10.get(i))) {
                                int elementCount = Integer.parseInt(hashmap.get(matchstring10.get(i)).toString());
                                elementCount++;
                                hashmap.put(matchstring10.get(i), elementCount);
                                hashmap1.put(matchstring10.get(i), elementCount);
                            } else {
                                uniqueSet.add(matchstring10.get(i));
                                hashmap.put(matchstring10.get(i), 1);
                                hashmap1.put(matchstring10.get(i), 1);
                            }
                        }
                    }

                }

                //*******************************Find Phenotype (Website Link)**************************/////
            }
            br.close();
            writer.write("Users who posted URL: "+ urlUserMap.size()+"\n");
            SortedSet<Map.Entry<String, Integer>> sortedJournals = entriesSortedByValues(hashmap);
            for (Map.Entry<String, Integer> ent : sortedJournals) {
                writer.write(ent.getKey() + "\t" + ent.getValue() + '\n');
                //System.out.println(ent.getKey() + "\t" + ent.getValue());
            }
            
            
            writer.close();

        }

    }

    static class ValueComparator<K, V extends Comparable<V>> implements Comparator<K> {

        Map<K, V> map;

        public ValueComparator(Map<K, V> base) {
            this.map = base;
        }

        @Override
        public int compare(K o1, K o2) {
            return map.get(o2).compareTo(map.get(o1));
        }
    }

    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public static LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String) key, (Double) val);
                    break;
                }

            }

        }
        return sortedMap;
    }
}
