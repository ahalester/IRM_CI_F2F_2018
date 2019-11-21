package de.obopstest.api.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiUtils {

    private static Logger LOG = LoggerFactory.getLogger(ApiUtils.class);

    public static void replaceAll(@NotNull StringBuffer sb, String regex, String replacement)
    {
        String aux = sb.toString();
        aux = aux.replaceAll(regex, replacement);
        sb.setLength(0);
        sb.append(aux);
    }

    @NotNull
    public static String encodeOusUid(String ousUid){
        StringBuffer strb = new StringBuffer(ousUid);

        replaceAll(strb,":","_");
        replaceAll(strb,"/","_");

        return strb.toString();
    }
}
