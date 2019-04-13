/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.sk1ercommon;

import cc.hyperium.utils.JsonHolder;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class Sk1erMod {
    private static Sk1erMod instance;
    private String modid;
    private String version;
    private boolean enabled = true;
    private JsonHolder en = new JsonHolder();

    public Sk1erMod(String modid, String version) {
        this.modid = modid;
        this.version = version;
        instance = this;
    }

    public Sk1erMod(String modid, String version, GenKeyCallback callback) {
        this(modid, version);
    }

    public static Sk1erMod getInstance() {
        return instance;
    }

    public JsonHolder getResponse() {
        return en;
    }

    public boolean isEnabled() {
        return true;
    }

    public String rawWithAgent(String url) {
        url = url.replace(" ", "%20");
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (" + modid + " V" + version + ") via Hyperium ");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
