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
public class WebMD_jinaFormat {

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
        Pattern p8 = Pattern.compile(".{1,50}www.{1,500}|.{1,50}http.{1,500}");
        String[] fileNames = {"addiction", "adhd", "breast_cancer", "diabetes", "diet", "fkids", "heart", "ms", "pain", "sexualhealth"};
//        String[] fileNames = {"diabetes"};
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
            int userthread = 0;
            hashmap = new HashMap<String, Integer>();
            hashmap1 = new HashMap<String, Integer>();
            userMap = new HashMap<String, Integer>();
            helProMap = new HashMap<String, Integer>();
            staffMap = new HashMap<String, Integer>();
            uniqueSet = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader("data/webmd_" + fileLog + ".csv"));
            urlUserMap = new HashMap<String, Integer>();
            BufferedWriter writer = new BufferedWriter(new FileWriter("test/webmd_" + fileLog + "_jinaFormat.txt"));

            while (br.ready()) {

                String check = br.readLine();
                String[] tokenizedTerms1 = check.split("<tpr>");
                //String nameToken1 = tokenizedTerms1[3];
                String[] tokenizedTerms = check.split(",");
                String token = tokenizedTerms[0];
                String nameToken = tokenizedTerms1[1];

                if ((nameToken.length() > 3) && (nameToken.charAt(nameToken.length() - 1) == ' ')) {
                    nameToken = nameToken.substring(0, nameToken.length() - 1);
                }
                //if (check.contains("http") || check.contains("www")) {
                if (nameToken.toUpperCase().contains(", MSN") || nameToken.toUpperCase().contains(", RNP") || nameToken.toUpperCase().contains(", CDOE") || nameToken.toUpperCase().contains(", MD") || nameToken.toUpperCase().contains(", MPH") || nameToken.toUpperCase().contains(", PHD") || nameToken.toUpperCase().contains(", PT")
                        || nameToken.toUpperCase().contains(", DSc") || nameToken.toUpperCase().contains(", NCS") || nameToken.toUpperCase().contains(", MSCS") || nameToken.toUpperCase().contains(", RN") || nameToken.toUpperCase().contains("_MSN") || nameToken.toUpperCase().contains("_RNP")
                        || nameToken.toUpperCase().contains("_CDOE") || nameToken.toUpperCase().contains("_MD") || nameToken.toUpperCase().contains("_MPH") || nameToken.toUpperCase().contains("_PHD") || nameToken.toUpperCase().contains("_PT") || nameToken.toUpperCase().contains("_DSc")
                        || nameToken.toUpperCase().contains("_NCS") || nameToken.toUpperCase().contains("_MSCS") || nameToken.toUpperCase().contains("_RN")) {
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
                    if (check.contains("http") || check.contains("www")) {
                        urlUserCount++;
                    }
                } else {
                    userMap.put(nameToken, userMap.get(nameToken) + 1);
                    if (check.contains("http") || check.contains("www")) {
                        urlUserCount++;
                    }
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
            br = new BufferedReader(new FileReader("data/webmd_" + fileLog + ".csv"));
            int userSize = sortedUserMap.size();
            //System.out.println("sorted size: " + sortedUserMap.size() +" original size"+userMap.size() );
            int top25 = userSize / 4;
            int mid50 = userSize - top25 * 2;
            HelPro = helProMap.size();
            staffCount = staffMap.size();
            System.out.println("**" + fileLog + " Log **");
//            System.out.println("Q1 Users: "+ top25);
//            System.out.println("Q2~Q3 Users: "+ mid50);
//            System.out.println("Q4 User: "+ top25);
//            System.out.println("All users :" + userMap.size());
//            System.out.println("Health Professional User Count: "+ helProMap.size());
//            System.out.println("Health Professional User Count: "+ helProMap.size());
//            System.out.println("Staff Members Count: "+ staffMap.size());
            System.out.println("All users Posts Count :" + addPosts(userMap));
//              System.out.println("Health Professional Post Count: "+ addPosts(helProMap));
//              System.out.println("Staff Members Post Count: "+ addPosts(staffMap));
//            System.out.println(staffPost+"  HP--> "+helProPost); 
//            System.out.println(helProMap.toString());
            System.out.println(userMap.toString());

            //         System.out.println("Health Professional User Count: "+ sortedUserMap.toString());
            mid50 = userSize - top25;
            int index = 0;
            int u1 = 0;
            int u2 = 0;
            int u3 = 0;
            for (Map.Entry<String, Integer> ent : sortedUserMap) {
                if (index <= top25) {
                    u1 = u1 + ent.getValue();
                } else if ((index <= mid50) && (index > top25)) {
                    u2 = u2 + ent.getValue();
                } else if (index > mid50) {
                    u3 = u3 + ent.getValue();
                }
                index++;
            }
            System.out.println("All Posts Count for Q1 " + u1);
            System.out.println("All Posts Count for Q2 " + u2);
            System.out.println("All Posts Count for Q4 " + u3);

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
            String bheck = br.readLine();
            /*Uncomment these two lines */
//            writer.write("posterID"+"\t"+"qid"+"\t"+"post ID"+"\t"+"User Group"+"\t"+"Hon Code"+"\t"+"Website Category"+"\t"+"Community"+"\t"+"exact URL"+"\n");
//            writer.write("\n");
            while (br.ready()) {
                String check = br.readLine();
                String[] tokenizedTerms1 = check.split("<tpr>");
                //String nameToken1 = tokenizedTerms1[3];
                String[] tokenizedTerms = check.split(",");
                
                String nameToken = tokenizedTerms1[1];
                String[] coloumn = check.split(",\"");
                
                String qid = tokenizedTerms[1];
                String postID = tokenizedTerms[0];
                String userGroup = null;
                String community = fileLog;
                        
                if ((nameToken.length() > 3) && (nameToken.charAt(nameToken.length() - 1) == ' ')) {
                    nameToken = nameToken.substring(0, nameToken.length() - 1);
                }
                String posterID = nameToken;
                /*
                 set the condition of the if loop to:
                 For 1st Quartile: (selectedUserIndex < mid50)&&(selectedUserIndex <= top25)&&(selectedUserIndex != -1)
                 For 2nd+3rd Quartile: (selectedUserIndex <= mid50)&&(selectedUserIndex > top25)&&(selectedUserIndex != -1)
                 For 4th Quartile: (selectedUserIndex > mid50)&&(selectedUserIndex > top25)&&(selectedUserIndex != -1)
                 For Health professionals: helProMap.containsKey(nameToken)
                 For WebMD_Staff : staffMap.containsKey(nameToken)
                 */
                int selectedUserIndex = sortedUserList.indexOf(nameToken);
                if ((selectedUserIndex < mid50) && (selectedUserIndex <= top25) && (selectedUserIndex != -1)) 
                    userGroup = "Q1";
                else if ((selectedUserIndex <= mid50) && (selectedUserIndex > top25) && (selectedUserIndex != -1)) 
                    userGroup = "Q2";
                else if ((selectedUserIndex > mid50) && (selectedUserIndex > top25) && (selectedUserIndex != -1)) 
                    userGroup = "Q3";
                
                if(selectedUserIndex == -1){
                    if(helProMap.get(nameToken) != null)
                        userGroup = "Health Professional";
                    else if(staffMap.get(nameToken) != null)
                        userGroup = "WebMd Staff";
                }
                if(userGroup == null)
                    System.out.println("ERROR !! userGroup not assigned "+postID + nameToken );
                selectedUserIndex = sortedUserList.indexOf(nameToken);
                
                
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
//                        System.out.println(m9.group());
                    }

                    //*******************************Unique set created and items put in HashMap (hashMap)**************************/////
                    
                    for (int i = 0; i < matchstring9.size(); i++) {
                        int honCode = 0;
                        String category = "Other";
                        System.out.println(matchstring9.get(i));
                        String url = matchstring9.get(i);
                        /* Assigning the honcode*/
                        if(url.contains("www.webmd.com/help"))
                            honCode = 0;
                        else if (url.contains("answers.webmd.com") || url.contains("arthritis.webmd.com") || url.contains("children.webmd.com") || url.contains("customercare.webmd.com") || url.contains("doctor.webmd.com") || url.contains("men.webmd.com") || url.contains("symptoms.webmd.com") || url.contains("women.webmd.com") || url.contains("medscape.com") || url.contains("boards.webmd") || url.contains("forums.webmd") || url.contains("www.webmd.com") || url.contains("diabetes.webmd.com"))
                            honCode = 3;
                        else if (url.contains("a-fib.com") || url.contains("dlife.com") || url.contains("www.drugs.com") || url.contains("emedicinehealth.com") || url.contains("medicinenet.com") || url.contains("needymeds.com"))
                            honCode = 2;
                        else if (url.contains("ehealthmd.com") || url.contains("iguard.org") || url.contains("intelihealth.com") || url.contains("lifebeatonline.com") || url.contains("mayoclinic.com") || url.contains("rxlist.com") || url.contains("suboxone.com") || url.contains("texasheartinstitute.org")  || url.contains("heartsite.com") || url.contains("medhelp.org") || url.contains("medicalnewstoday.com") || url.contains("merck.com") || url.contains("msaa.org") || url.contains("mult-sclerosis.org") || url.contains("ncvc.org") || url.contains("spine-health.com") || url.contains("spineuniverse.com") || url.contains("client.myoptumhealth.com") || url.contains("americanheart.org") || url.contains("diabetes.org")                                 || url.contains("blogs.webmd.com") || url.contains("forums.webmd.com") || url.contains("exchanges.webmd.com") || url.contains("boards.webmd.com") || url.contains("familydoctor.org") || url.contains("labtestsonline.org") || url.contains("my.clevelandclinic.org") || url.contains("americanheart.org") || url.contains("drugstore.com") || url.contains("everydayhealth.com") || url.contains("healthcentral.com") || url.contains("healthline.com"))
                            honCode = 1;
                        /* Assigning the url Category*/
                        if (url.contains(".org")) {
                            category = "org";
                        }
                        if (url.contains(".edu")) {
                            category = "edu";
                        }
                        if (url.contains(".gov")) {
                            category = "gov";
                        }
                        if (url.contains(".webmd")) {
                            category = "webmd";
                        }
                        if(url.contains("facebook")||url.contains("twitter")||url.contains("linkedin")||url.contains("pintrest")||url.contains("plus.google")||url.contains("instagram.com")||url.contains("vk.com")||url.contains("flickr.com")||url.contains("vine.com")||url.contains("meetup.com")||url.contains("tagged")||url.contains("ask.fm")||url.contains("meetme.com")||url.contains("classmates.com"))
                            category = "Social Media";
                        writer.write(posterID +"\t"+qid+"\t"+postID+"\t"+userGroup+"\t"+honCode+"\t"+category+"\t"+community+"\t"+url +"\n");
                    }
                }
                else
                    writer.write(posterID +"\t"+qid+"\t"+postID+"\t"+userGroup+"\t"+"N/A"+"\t"+"N/A"+"\t"+community+"\t"+"N/A"+"\n");


                
//                System.out.println(posterID +"\t"+qid+"\t"+postID+"\t"+userGroup+"\t"+"HonCode"+"\t"+"category"+"\t"+community+"\t"+"exactURL" );

                //*******************************Find Phenotype (Website Link)**************************/////
            }
            br.close();
//            writer.write("Users who posted URL: " + urlUserMap.size() + "\n");
            System.out.println("Present Group Post Count: " + addPosts(urlUserMap) + "  " + userthread);
            SortedSet<Map.Entry<String, Integer>> sortedJournals = entriesSortedByValues(hashmap);
            for (Map.Entry<String, Integer> ent : sortedJournals) {
//                writer.write(ent.getKey() + "\t" + ent.getValue() + '\n');
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
    
    public static List<String> extractUrls(String input) {
        List<String> result = new ArrayList<String>();

        Pattern pattern = Pattern.compile(
                "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)"
                + "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov"
                + "|mil|biz|info|mobi|name|aero|jobs|museum"
                + "|travel|[a-z]{2}))(:[\\d]{1,5})?"
                + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?"
                + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
                + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)"
                + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
                + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
                + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }
    
    public static int addPosts(HashMap<String, Integer> map) {
        int postCount = 0;
        for (int value : map.values()) {
            postCount = postCount + value;
        }
        return postCount;
    }
}
