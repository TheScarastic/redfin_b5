package com.android.systemui.statusbar.notification.logging.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class Notifications$NotificationList extends MessageNano {
    public Notifications$Notification[] notifications;

    public Notifications$NotificationList() {
        clear();
    }

    public Notifications$NotificationList clear() {
        this.notifications = Notifications$Notification.emptyArray();
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        Notifications$Notification[] notifications$NotificationArr = this.notifications;
        if (notifications$NotificationArr != null && notifications$NotificationArr.length > 0) {
            int i = 0;
            while (true) {
                Notifications$Notification[] notifications$NotificationArr2 = this.notifications;
                if (i >= notifications$NotificationArr2.length) {
                    break;
                }
                Notifications$Notification notifications$Notification = notifications$NotificationArr2[i];
                if (notifications$Notification != null) {
                    codedOutputByteBufferNano.writeMessage(1, notifications$Notification);
                }
                i++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        Notifications$Notification[] notifications$NotificationArr = this.notifications;
        if (notifications$NotificationArr != null && notifications$NotificationArr.length > 0) {
            int i = 0;
            while (true) {
                Notifications$Notification[] notifications$NotificationArr2 = this.notifications;
                if (i >= notifications$NotificationArr2.length) {
                    break;
                }
                Notifications$Notification notifications$Notification = notifications$NotificationArr2[i];
                if (notifications$Notification != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, notifications$Notification);
                }
                i++;
            }
        }
        return computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public Notifications$NotificationList mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                Notifications$Notification[] notifications$NotificationArr = this.notifications;
                int length = notifications$NotificationArr == null ? 0 : notifications$NotificationArr.length;
                int i = repeatedFieldArrayLength + length;
                Notifications$Notification[] notifications$NotificationArr2 = new Notifications$Notification[i];
                if (length != 0) {
                    System.arraycopy(notifications$NotificationArr, 0, notifications$NotificationArr2, 0, length);
                }
                while (length < i - 1) {
                    notifications$NotificationArr2[length] = new Notifications$Notification();
                    codedInputByteBufferNano.readMessage(notifications$NotificationArr2[length]);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                notifications$NotificationArr2[length] = new Notifications$Notification();
                codedInputByteBufferNano.readMessage(notifications$NotificationArr2[length]);
                this.notifications = notifications$NotificationArr2;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
