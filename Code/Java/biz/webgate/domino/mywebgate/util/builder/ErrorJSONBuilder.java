 /*
 * � Copyright WebGate Consulting AG, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package biz.webgate.domino.mywebgate.util.builder;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.domino.services.util.JsonWriter;

public class ErrorJSONBuilder {

	private static ErrorJSONBuilder m_Builder;

	private ErrorJSONBuilder() {

	}

	public static synchronized ErrorJSONBuilder getInstance() {
		if (m_Builder == null) {
			m_Builder = new ErrorJSONBuilder();
		}
		return m_Builder;
	}

	public void processError2JSON(RestServiceEngine engine, int nError,
			String strError, Exception ex) {
		try {
			engine.getHttpResponse().getWriter().flush();
			JsonWriter jsWriter = new JsonWriter(engine.getHttpResponse()
					.getWriter(), true);
			jsWriter.startObject();
			jsWriter.startProperty("error");
			jsWriter.outStringLiteral(strError);
			jsWriter.endProperty();
			jsWriter.startProperty("errornr");
			jsWriter.outIntLiteral(nError);
			jsWriter.endProperty();
			if (ex != null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				jsWriter.startProperty("trace");
				jsWriter.outStringLiteral(sw.toString());
				jsWriter.endProperty();
			}
			jsWriter.endObject();
			jsWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
