package net;

import gae.GAEDataBase;
import gae.GAENodeCase;
import gae.GAENodeDebug;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tool.TypeFilter;

public class ReadUrlByPOST {

	static String add_caseURL = "http://www.czone2.tcg.gov.tw/Gmaps/add_case.cfm";
	static String preview_caseURL = "http://www.czone2.tcg.gov.tw/tp95-4/sys/preview_case.cfm";
	static String imagePath = "/Users/ggm/Desktop/case_pic.JPG";
	
	public static void main(String args[])
	{
		String r="查報序號:09911-704980 查報日期:99年11月6日大類:m1 細類:區2 鄰近路段: ";
		System.out.println(getSno(r));
		
	}
	
	//return key
	public static String doSend(GAENodeCase node) {

		HashMap<String, String> forms = createForm(node);
		GAEDataBase.store(new GAENodeDebug("res","ReadUrlByPost"+"--"+"doSend"));
		
		try {
			System.out.println("forms: \n"+forms.toString());
			SendPost sendpost = new SendPost(add_caseURL);
			sendpost.setTextPatams(forms);
			
			GAEDataBase.store(new GAENodeDebug("forms",forms.toString()));

//			return "done";

			byte img[] = node.getImage(0);
			if(img!=null)
				sendpost.addByteParameter("map",img);
			
			byte[] res_bytes = sendpost.send();
			String res = new String(res_bytes,"UTF-8");
			String sno = getSno(res); 

			res = HtmlFilter.parseHTMLStr(res);
			res = HtmlFilter.delSpace(res);

			GAEDataBase.store(new GAENodeDebug("res",res.substring(0,50)));
			GAEDataBase.store(new GAENodeDebug("res-sno",sno));
			
			System.out.println("sno: "+sno+"czone result:"+res);
			return sno;

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return e.toString()+"\n\n deSend ";
		}
	}

	private static HashMap<String, String> createForm(GAENodeCase node) {
		HashMap<String, String> forms = new HashMap<String, String>();

		forms.put("sno","09908-521171");
//		forms.put("unit" ,"區民政課");
		forms.put("h_item1" ,TypeFilter.Id2Type1(node.typeid));
		forms.put("h_item2" ,TypeFilter.Id2Type2(node.typeid));
//		forms.put("h_item1" ,"m1");
//		forms.put("h_item2" ,"區2");
		forms.put("h_roadname" ,"");
		forms.put("h_parkname" ,"");
		forms.put("h_unitname" ,"");

		forms.put("h_admit_name" ,node.h_admit_name);
		forms.put("h_admiv_name" ,node.h_admiv_name);
		forms.put("h_summary" ,node.h_summary);
		forms.put("h_memo" ,"地點:"+node.address);
		forms.put("pt_name" ,"001001");
		forms.put("h_pname" ,node.name);
		forms.put("h_punit" ,"民眾");
		forms.put("h_ptel" ,"");
		forms.put("h_pfax" ,"");
		forms.put("h_pemail" ,node.email);
		forms.put("h_x1" ,String.valueOf(node.coordx));
		forms.put("h_y1" ,String.valueOf(node.coordy));
		forms.put("h_width" ,"0");
		forms.put("h_ddx" ,String.valueOf(node.coordx));
		forms.put("h_ddy" ,String.valueOf(node.coordy));
		
		forms.put("input_type" ,"extan");
		forms.put("case_type" ,"0");
		
		forms.put("h_pic1" ,"");
		forms.put("h_pic2" ,"");
		forms.put("h_per" ,"0");
		forms.put("h_item4" ,"");

		if(node.photo!=null && node.photo.length!=0)
		{
			forms.put("pic_check" ,"1");
			forms.put("h_pic" ,"1");
		}
		else
		{
			forms.put("pic_check" ,"0");
			forms.put("h_pic" ,"");
		}

		node.unit = TypeFilter.typeid2unit(node.typeid);
		forms.put("unit",node.unit);

		return forms;
	}

    public static String getSno(String htmlcontext){
        try {
                Matcher matcher;
                matcher = Pattern.compile("[0-9]{5}-[0-9]{6}", Pattern.MULTILINE).matcher(htmlcontext);
                String res = "";

                for(res = "";matcher.find();)
                        res = matcher.group();
                return res;

        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        return null;
    }

	
	
}
