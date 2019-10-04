/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.KeyPurposeId;

public class ExtendedKeyUsage
extends ASN1Object {
    ASN1Sequence seq;
    Hashtable usageTable = new Hashtable();

    public ExtendedKeyUsage(Vector vector) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            KeyPurposeId keyPurposeId = KeyPurposeId.getInstance(enumeration.nextElement());
            aSN1EncodableVector.add(keyPurposeId);
            this.usageTable.put((Object)keyPurposeId, (Object)keyPurposeId);
        }
        this.seq = new DERSequence(aSN1EncodableVector);
    }

    private ExtendedKeyUsage(ASN1Sequence aSN1Sequence) {
        this.seq = aSN1Sequence;
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            ASN1Encodable aSN1Encodable = (ASN1Encodable)enumeration.nextElement();
            if (!(aSN1Encodable.toASN1Primitive() instanceof ASN1ObjectIdentifier)) {
                throw new IllegalArgumentException("Only ASN1ObjectIdentifiers allowed in ExtendedKeyUsage.");
            }
            this.usageTable.put((Object)aSN1Encodable, (Object)aSN1Encodable);
        }
    }

    public ExtendedKeyUsage(KeyPurposeId keyPurposeId) {
        this.seq = new DERSequence(keyPurposeId);
        this.usageTable.put((Object)keyPurposeId, (Object)keyPurposeId);
    }

    public ExtendedKeyUsage(KeyPurposeId[] arrkeyPurposeId) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 != arrkeyPurposeId.length; ++i2) {
            aSN1EncodableVector.add(arrkeyPurposeId[i2]);
            this.usageTable.put((Object)arrkeyPurposeId[i2], (Object)arrkeyPurposeId[i2]);
        }
        this.seq = new DERSequence(aSN1EncodableVector);
    }

    public static ExtendedKeyUsage fromExtensions(Extensions extensions) {
        return ExtendedKeyUsage.getInstance(extensions.getExtensionParsedValue(Extension.extendedKeyUsage));
    }

    public static ExtendedKeyUsage getInstance(Object object) {
        if (object instanceof ExtendedKeyUsage) {
            return (ExtendedKeyUsage)object;
        }
        if (object != null) {
            return new ExtendedKeyUsage(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static ExtendedKeyUsage getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return ExtendedKeyUsage.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public KeyPurposeId[] getUsages() {
        KeyPurposeId[] arrkeyPurposeId = new KeyPurposeId[this.seq.size()];
        int n2 = 0;
        Enumeration enumeration = this.seq.getObjects();
        while (enumeration.hasMoreElements()) {
            int n3 = n2 + 1;
            arrkeyPurposeId[n2] = KeyPurposeId.getInstance(enumeration.nextElement());
            n2 = n3;
        }
        return arrkeyPurposeId;
    }

    public boolean hasKeyPurposeId(KeyPurposeId keyPurposeId) {
        return this.usageTable.get((Object)keyPurposeId) != null;
    }

    public int size() {
        return this.usageTable.size();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
