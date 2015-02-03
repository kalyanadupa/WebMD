/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webmd;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Kalyan
 */
public class WebMD_Other {

     /**
     * @param args
     * @throws IOException
     *
     */
    public static void main(String[] args) throws IOException {

        HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
        HashMap<String, Integer> userMap = new HashMap<String, Integer>();
        List<String> uniqueSet = new ArrayList<String>();
        Pattern p8 = Pattern.compile(".{1,50}www.{1,500}");
        String[] fileNames = {"webmd_addiction", "webmd_adhd", "webmd_breast_cancer", "webmd_diabetes", "webmd_diet", "webmd_fkids", "webmd_heart", "webmd_ms", "webmd_pain", "webmd_sexualhealth"};
        //*******************************For the matchings of staff and qid**************************/////

        for (String fileLog : fileNames) {
            hashmap = new HashMap<String, Integer>();
            userMap = new HashMap<String, Integer>();
            uniqueSet = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader("data/" + fileLog + ".csv"));
            List<String> qidArray = new ArrayList<String>();

            BufferedWriter writer = new BufferedWriter(new FileWriter("test/" + fileLog + "_staff_qid.txt"));

            while (br.ready()) {
                String check = br.readLine();
                String[] tokenizedTerms = check.split("\"");
                String nameToken = tokenizedTerms[3];
                //[ MSN, RNP, CDOE, MD, MPH, PHD, PT, DSc, NCS, MSCS]
                //[ RN]
                //System.out.println(nameToken);

                    if ((check.contains("www")) | (check.contains("http"))) {
                        
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
                                } else {
                                    uniqueSet.add(matchstring10.get(i));
                                    hashmap.put(matchstring10.get(i), 1);
                                }
                            }
                        }

                    
            }
            br.close();
            SortedSet<Map.Entry<String, Integer>> sortedJournals = entriesSortedByValues(hashmap);
            for (Entry<String, Integer> ent : sortedJournals) {
//                if (ent.getKey().contains(".org") ||ent.getKey().contains(".edu") ||ent.getKey().contains(".gov") ||ent.getKey().contains("exchanges.webmd") || ent.getKey().contains("blogs.webmd") || ent.getKey().contains("facebook") || ent.getKey().contains("youtube") || ent.getKey().contains("intherooms") || ent.getKey().contains("friendsofbill") || ent.getKey().contains("rightactionforwomen") || ent.getKey().contains("hillbillyhousewife") || ent.getKey().contains("boards.webmd") || ent.getKey().contains("foxnews")) {
//                    System.out.println("");
//                }
//                else{
                    writer.write(ent.getKey() + "\t" + ent.getValue() + '\n');
                    System.out.println(ent.getKey() + "\t" + ent.getValue());                    
//                }
                    
            }

            writer.close();
        }

//        //*******************************For the matchings of  qid**************************/////
//        System.out.println("for qid ");
//        System.out.println(""); 
//       for (String fileLog : fileNames) {
//            hashmap = new HashMap<String, Integer>();
//            uniqueSet = new ArrayList<String>();
//            BufferedReader br = new BufferedReader(new FileReader("data/" + fileLog + ".csv"));
//            List<String> qidArray = new ArrayList<String>();
//            BufferedReader in = new BufferedReader(new FileReader("data/" + fileLog + "_staff_qid.csv")); // qid file 
//
//            BufferedWriter writer2 = new BufferedWriter(new FileWriter("newTest/"+fileLog + "_qid" + ".txt"));
//
//            String s = in.readLine();
//            while ((s = in.readLine()) != null) {
//                qidArray.add(s);
//            }
//
//            while (br.ready()) {
//                String check = br.readLine();
//                String[] tokenizedTerms = check.split(",");
//                String token = tokenizedTerms[1];
//                String nameToken = tokenizedTerms[3];
//                String[] tokenizedTerms1 = nameToken.split("_");
//
//                //*******************************Find Phenotype (Website Link)**************************/////
//                if ((qidArray.contains(token)) && ((check.contains("www")) | (check.contains("http")))) {
//
//                    String check2 = " ";
//                    String check3 = " ";
//                    Matcher m8 = p8.matcher(check);
//                    List<String> matchstring8 = new ArrayList<String>();
//
//                    while (m8.find()) {
//                        check2 = check2 + "" + m8.group();
//                        matchstring8.add(m8.group());
////                    System.out.println(m8.group());
//                    }
//
//                    Pattern p9 = Pattern.compile("https?\\:\\/\\/[\\-w\\.]*(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]|www\\.(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]");
//                    Matcher m9 = p9.matcher(check2);
//                    List<String> matchstring9 = new ArrayList<String>();
//                    while (m9.find()) {
//                        check3 = check3 + "" + m9.group();
//                        matchstring9.add(m9.group());
////                      System.out.println(m9.group());
//                    }
//
//                    Pattern p10 = Pattern.compile("www.*?(\\.com(?=\\W)|\\.com\\,?\\)?|\\.org(?=\\W)|\\.ORG(?=\\W)|\\.net(?=\\W)|\\.gov(?=\\W)|\\.co.uk|\\.html|\\.htm|\\.asp|\\.aspx|\\.edu|\\.us|treatment|\\-men(?=\\W)|\\.pdf|\\.ca\\/servlet|\\.ch(?=\\W)|\\.coream(?=\\W)|\\.ee(?=\\W))|www.*?(?=\\/)|(?<=http\\:\\/\\/).*?(?=\\/)|(?<=https\\:\\/\\/).*?(?=\\/)");
//                    Matcher m10 = p10.matcher(check3);
//                    List<String> matchstring10 = new ArrayList<String>();
//
//                    while (m10.find()) {
//                        matchstring10.add(m10.group());
////				  System.out.println(m10.group());
//
//                        //*******************************Unique set created and items put in HashMap (hashMap)**************************/////
//                        for (int i = 0; i < matchstring10.size(); i++) {
//                            if (uniqueSet.contains(matchstring10.get(i))) {
//                                int elementCount = Integer.parseInt(hashmap.get(matchstring10.get(i)).toString());
//                                elementCount++;
//                                hashmap.put(matchstring10.get(i), elementCount);
//                            } else {
//                                uniqueSet.add(matchstring10.get(i));
//                                hashmap.put(matchstring10.get(i), 1);
//                            }
//                        }
//                    }
//
//                }
//
//            }
//            br.close();
//
//		//*******************************For printing hashmap in console (without sorting)**************************/////
//		/*System.out.println(hashmap);        
//             Map<String, Integer> map = new TreeMap<String, Integer>(hashmap);
//             System.out.println(map);*/
//            //*******************************For Sorting hashmap according to frequency**************************/////
//            
//            writer2.close();
//        }
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
