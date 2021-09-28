package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.options.ParseOptions;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/* loaded from: classes.dex */
public class XMPMetaParser {
    public static final Object XMP_RDF = new Object();
    public static DocumentBuilderFactory factory;

    static {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        newInstance.setNamespaceAware(true);
        newInstance.setIgnoringComments(true);
        try {
            newInstance.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        } catch (Exception unused) {
        }
        factory = newInstance;
    }

    public static Object[] findRootNode(Node node, boolean z, Object[] objArr) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (7 == item.getNodeType()) {
                ProcessingInstruction processingInstruction = (ProcessingInstruction) item;
                if (processingInstruction.getTarget() == "xpacket") {
                    if (objArr != null) {
                        objArr[2] = processingInstruction.getData();
                    }
                }
            }
            if (!(3 == item.getNodeType() || 7 == item.getNodeType())) {
                String namespaceURI = item.getNamespaceURI();
                String localName = item.getLocalName();
                if (("xmpmeta".equals(localName) || "xapmeta".equals(localName)) && "adobe:ns:meta/".equals(namespaceURI)) {
                    return findRootNode(item, false, objArr);
                }
                if (z || !"RDF".equals(localName) || !"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI)) {
                    Object[] findRootNode = findRootNode(item, z, objArr);
                    if (findRootNode != null) {
                        return findRootNode;
                    }
                } else {
                    if (objArr != null) {
                        objArr[0] = item;
                        objArr[1] = XMP_RDF;
                    }
                    return objArr;
                }
            }
        }
        return null;
    }

    public static Document parseInputSource(InputSource inputSource) throws XMPException {
        try {
            DocumentBuilder newDocumentBuilder = factory.newDocumentBuilder();
            newDocumentBuilder.setErrorHandler(null);
            return newDocumentBuilder.parse(inputSource);
        } catch (IOException e) {
            throw new XMPException("Error reading the XML-file", 204, e);
        } catch (ParserConfigurationException e2) {
            throw new XMPException("XML Parser not correctly configured", 0, e2);
        } catch (SAXException e3) {
            throw new XMPException("XML parsing failure", 201, e3);
        }
    }

    public static Document parseXmlFromBytebuffer(ByteBuffer byteBuffer, ParseOptions parseOptions) throws XMPException {
        try {
            return parseInputSource(new InputSource(byteBuffer.getByteStream()));
        } catch (XMPException e) {
            if (e.getErrorCode() == 201 || e.getErrorCode() == 204) {
                if (parseOptions.getOption(16) && "UTF-8".equals(byteBuffer.getEncoding())) {
                    byte[] bArr = new byte[8];
                    ByteBuffer byteBuffer2 = new ByteBuffer((byteBuffer.length * 4) / 3);
                    int i = 0;
                    boolean z = false;
                    int i2 = 0;
                    int i3 = 0;
                    while (true) {
                        int i4 = byteBuffer.length;
                        if (i >= i4) {
                            if (z) {
                                for (int i5 = 0; i5 < i2; i5++) {
                                    byteBuffer2.append(Latin1Converter.convertToUTF8(bArr[i5]));
                                }
                            }
                            byteBuffer = byteBuffer2;
                        } else if (i < i4) {
                            int i6 = byteBuffer.buffer[i] & 255;
                            if (z) {
                                if (i3 <= 0 || (i6 & 192) != 128) {
                                    byteBuffer2.append(Latin1Converter.convertToUTF8(bArr[0]));
                                    i -= i2;
                                } else {
                                    int i7 = i2 + 1;
                                    bArr[i2] = (byte) i6;
                                    i3--;
                                    if (i3 == 0) {
                                        byteBuffer2.ensureCapacity(byteBuffer2.length + i7);
                                        System.arraycopy(bArr, 0, byteBuffer2.buffer, byteBuffer2.length, i7);
                                        byteBuffer2.length += i7;
                                    } else {
                                        i2 = i7;
                                    }
                                }
                                z = false;
                                i2 = 0;
                            } else if (i6 < 127) {
                                byteBuffer2.ensureCapacity(byteBuffer2.length + 1);
                                byte[] bArr2 = byteBuffer2.buffer;
                                int i8 = byteBuffer2.length;
                                byteBuffer2.length = i8 + 1;
                                bArr2[i8] = (byte) i6;
                            } else if (i6 >= 192) {
                                i3 = -1;
                                int i9 = i6;
                                while (i3 < 8 && (i9 & 128) == 128) {
                                    i3++;
                                    i9 <<= 1;
                                }
                                bArr[i2] = (byte) i6;
                                i2++;
                                z = true;
                            } else {
                                byteBuffer2.append(Latin1Converter.convertToUTF8((byte) i6));
                            }
                            i++;
                        } else {
                            throw new IndexOutOfBoundsException("The index exceeds the valid buffer area");
                        }
                    }
                }
                if (!parseOptions.getFixControlChars()) {
                    return parseInputSource(new InputSource(byteBuffer.getByteStream()));
                }
                try {
                    return parseInputSource(new InputSource(new FixASCIIControlsReader(new InputStreamReader(byteBuffer.getByteStream(), byteBuffer.getEncoding()))));
                } catch (UnsupportedEncodingException unused) {
                    throw new XMPException("Unsupported Encoding", 9, e);
                }
            } else {
                throw e;
            }
        }
    }
}
