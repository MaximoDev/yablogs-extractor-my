package xdsoft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseWriter {

	/**
	 * 
	 * @param tableName
	 *            Имя таблицы в HBase
	 * @param keyName
	 *            Имя поля ключа HBase в передаваемой таблице ключей-значение
	 * @param records
	 *            Список (List) записей типа Map (Key-Value) <br />
	 *            <br />
	 *            <b>Example:</b> <i> List<Map> MapRecord1<String, String>
	 *            <br />
	 *            mr1_key1 - val1 <br />
	 *            mr1_key2 - val2 <br />
	 *            mr1_key3 - val3 <br />
	 * 
	 *            MapRecord2<String, String> <br />
	 *            mr2_key1 - val1 <br />
	 *            mr2_key2 - val2 <br />
	 *            mr2_key3 - val3 <br />
	 *            ... </i>
	 * @throws IOException
	 * 
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void writeToHBase(String tableName, String colFamily, String keyName, ArrayList<HashMap<String, String>> records)
			throws IOException {

		Configuration config = HBaseConfiguration.create();
		HTable hTable = new HTable(config, tableName);

		for (Object rec0 : records) {

			Map<String, String> mapRec = (HashMap<String, String>) rec0;
			String keyValue = mapRec.get(keyName);

			if (keyValue != null) {

				Put newHBaseRecord = new Put(Bytes.toBytes(keyValue));

				for (Map.Entry<String, String> mp : mapRec.entrySet()) {

					String key = mp.getKey();
					String value = mp.getValue();

					if (!value.isEmpty() && !keyName.equals(key)) {

						newHBaseRecord.add(Bytes.toBytes(colFamily), Bytes.toBytes(key), Bytes.toBytes(value));

						System.out.println("Writed: " + key + " - " + value);

					}

				}

				hTable.put(newHBaseRecord);

			}

		}

		hTable.close();

	}

}
