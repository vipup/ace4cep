package eu.blky.cep.weso.ace4cep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

public class CepPair {

	private String name;
	private List<UpdateListener> listSet = new ArrayList<UpdateListener>();
	private String text;
	
	public String toString() {
		
		String retval = name+":"+text+" ==> ";
		retval += array2string(listSet.toArray());
		return retval ;
	}
	String array2string(Object []oPar){
		String retval = "";
		for (Object o:oPar)retval+=o+" , ";
		return retval;
	}

	public CepPair(String name, Iterator<UpdateListener> listTmp) {
		this.setName(name);
		 
		while (listTmp.hasNext()   ) {
			UpdateListener l = listTmp.next(); 
			listSet .add(  l   );
		}
	}

	public CepPair(String name2, EPStatement stmtTmp) {
		this(name2, stmtTmp.getUpdateListeners());
		this.text = stmtTmp.getText();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
