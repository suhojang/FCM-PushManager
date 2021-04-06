package egovframework.com.cmm;

import org.apache.commons.collections.map.ListOrderedMap;

public class EgovMapNoCamel extends ListOrderedMap {
	private static final long serialVersionUID = 6723434363565852262L;

	public Object put(Object key, Object value) {
		return super.put((String) key, value);
	}
}