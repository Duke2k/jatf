/**
 * This file is part of JATF.
 * <p>
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * <p>
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Nullable;
import javax.tools.SimpleJavaFileObject;

public class SourceFile extends SimpleJavaFileObject {

	public SourceFile(File sourceFile) {
		this(sourceFile.toURI(), Kind.SOURCE);
	}

	@Override
	@Nullable
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		try (InputStream inputStream = uri.toURL().openStream()) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
			}
		}
		CharSequence result = stringBuilder.toString();
		return result.length() == 0 ? null : result;
	}

	private SourceFile(URI uri, Kind kind) {
		super(uri, kind);
	}
}
