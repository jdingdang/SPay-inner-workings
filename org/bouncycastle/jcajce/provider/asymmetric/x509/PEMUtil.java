package org.bouncycastle.jcajce.provider.asymmetric.x509;

class PEMUtil {
    private final String _footer1;
    private final String _footer2;
    private final String _header1;
    private final String _header2;

    PEMUtil(String str) {
        this._header1 = "-----BEGIN " + str + "-----";
        this._header2 = "-----BEGIN X509 " + str + "-----";
        this._footer1 = "-----END " + str + "-----";
        this._footer2 = "-----END X509 " + str + "-----";
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String readLine(java.io.InputStream r4) {
        /*
        r3 = this;
        r0 = new java.lang.StringBuffer;
        r0.<init>();
    L_0x0005:
        r1 = r4.read();
        r2 = 13;
        if (r1 == r2) goto L_0x0018;
    L_0x000d:
        r2 = 10;
        if (r1 == r2) goto L_0x0018;
    L_0x0011:
        if (r1 < 0) goto L_0x0018;
    L_0x0013:
        r1 = (char) r1;
        r0.append(r1);
        goto L_0x0005;
    L_0x0018:
        if (r1 < 0) goto L_0x0020;
    L_0x001a:
        r2 = r0.length();
        if (r2 == 0) goto L_0x0005;
    L_0x0020:
        if (r1 >= 0) goto L_0x0024;
    L_0x0022:
        r0 = 0;
    L_0x0023:
        return r0;
    L_0x0024:
        r0 = r0.toString();
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.jcajce.provider.asymmetric.x509.PEMUtil.readLine(java.io.InputStream):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    org.bouncycastle.asn1.ASN1Sequence readPEMObject(java.io.InputStream r4) {
        /*
        r3 = this;
        r0 = new java.lang.StringBuffer;
        r0.<init>();
    L_0x0005:
        r1 = r3.readLine(r4);
        if (r1 == 0) goto L_0x001b;
    L_0x000b:
        r2 = r3._header1;
        r2 = r1.startsWith(r2);
        if (r2 != 0) goto L_0x001b;
    L_0x0013:
        r2 = r3._header2;
        r1 = r1.startsWith(r2);
        if (r1 == 0) goto L_0x0005;
    L_0x001b:
        r1 = r3.readLine(r4);
        if (r1 == 0) goto L_0x0031;
    L_0x0021:
        r2 = r3._footer1;
        r2 = r1.startsWith(r2);
        if (r2 != 0) goto L_0x0031;
    L_0x0029:
        r2 = r3._footer2;
        r2 = r1.startsWith(r2);
        if (r2 == 0) goto L_0x0044;
    L_0x0031:
        r1 = r0.length();
        if (r1 == 0) goto L_0x0051;
    L_0x0037:
        r0 = r0.toString();	 Catch:{ Exception -> 0x0048 }
        r0 = org.bouncycastle.util.encoders.Base64.decode(r0);	 Catch:{ Exception -> 0x0048 }
        r0 = org.bouncycastle.asn1.ASN1Sequence.getInstance(r0);	 Catch:{ Exception -> 0x0048 }
    L_0x0043:
        return r0;
    L_0x0044:
        r0.append(r1);
        goto L_0x001b;
    L_0x0048:
        r0 = move-exception;
        r0 = new java.io.IOException;
        r1 = "malformed PEM data encountered";
        r0.<init>(r1);
        throw r0;
    L_0x0051:
        r0 = 0;
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.jcajce.provider.asymmetric.x509.PEMUtil.readPEMObject(java.io.InputStream):org.bouncycastle.asn1.ASN1Sequence");
    }
}