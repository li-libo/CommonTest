package test;
import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestStreamApi {
	public static void main(String[] args) {
	    List<String> list1 = new ArrayList<String>();
	    list1.add("1");
		list1.add("2");
		list1.add("3");
		list1.add("5");
		list1.add("6");
	 
	    List<String> list2 = new ArrayList<String>();
	    list2.add("2");
		list2.add("3");
		list2.add("7");
		list2.add("8");
	 
	    // 交集
	    List<String> intersection = list1.stream().filter(item -> list2.contains(item)).collect(toList());
	    System.out.println("---交集 intersection---");
	    intersection.parallelStream().forEach(System.out :: println);
	 
	    // 差集 (list1 - list2)
	    List<String> reduce1 = list1.stream().filter(item -> !list2.contains(item)).collect(toList());
	    System.out.println("---差集 reduce1 (list1 - list2)---");
	    reduce1.stream().forEach(System.out :: println);
	 
	    // 差集 (list2 - list1)
	    List<String> reduce2 = list2.stream().filter(item -> !list1.contains(item)).collect(toList());
	    System.out.println("---差集 reduce2 (list2 - list1)---");
	    reduce2.stream().forEach(System.out :: println);
	 
	    // 并集
	    List<String> listAll = list1.parallelStream().collect(toList());
	    List<String> listAll2 = list2.parallelStream().collect(toList());
	    listAll.addAll(listAll2);
	    System.out.println("---并集 listAll---");
	    listAll.parallelStream().forEachOrdered(System.out :: println);
	 
	    // 去重并集
	    List<String> listAllDistinct = listAll.stream().distinct().collect(toList());
	    System.out.println("---得到去重并集 listAllDistinct---");
	    listAllDistinct.parallelStream().forEachOrdered(System.out :: println);
	 
	    System.out.println("---原来的List1---");
	    list1.parallelStream().forEachOrdered(System.out :: println);
	    System.out.println("---原来的List2---");
	    list2.parallelStream().forEachOrdered(System.out :: println);
	    
	    List<String> list3 = new ArrayList<String>();
	    list3.add("A");
	    list3.add("B");
	    list3.add("C");

	    List<String> list4 = new ArrayList<String>();
	    list4.add("C");
	    list4.add("B");
	    list4.add("D");
	    // 并集
	    list3.addAll(list4);
	    // 去重复并集
	    list3.removeAll(list4);
	    list3.addAll(list4);
	    // 交集
	    list3.retainAll(list4);
	    // 差集
	    list3.removeAll(list4);	    
	    
	    System.out.println("---原来的List3---");
	    list3.parallelStream().forEachOrdered(System.out :: println);
	    System.out.println("---原来的List4---");
	    list4.parallelStream().forEachOrdered(System.out :: println);
	 
	}
	
	@Test
	public void testStream() {
		StringBuilder pageAdpsIdSb = new StringBuilder();
		for(int i=0; i<10; i++) {
			pageAdpsIdSb.append("a,");
		}
		pageAdpsIdSb.append("");
		List<String> aList = Arrays.asList(pageAdpsIdSb.toString().split(",")).stream().filter(x->!"".equals(x)).collect(toList());
		aList.forEach(x->System.out.println(x+"******"));
	}
}
