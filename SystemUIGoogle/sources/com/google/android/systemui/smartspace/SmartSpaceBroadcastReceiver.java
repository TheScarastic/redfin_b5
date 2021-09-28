package com.google.android.systemui.smartspace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.util.Log;
import androidx.constraintlayout.widget.R$styleable;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public class SmartSpaceBroadcastReceiver extends BroadcastReceiver {
    private final SmartSpaceController mController;

    public SmartSpaceBroadcastReceiver(SmartSpaceController smartSpaceController) {
        this.mController = smartSpaceController;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (SmartSpaceController.DEBUG) {
            Log.d("SmartSpaceReceiver", "receiving update");
        }
        int myUserId = UserHandle.myUserId();
        if (myUserId == 0) {
            if (!intent.hasExtra("uid")) {
                intent.putExtra("uid", myUserId);
            }
            byte[] byteArrayExtra = intent.getByteArrayExtra("com.google.android.apps.nexuslauncher.extra.SMARTSPACE_CARD");
            if (byteArrayExtra != null) {
                SmartspaceProto$SmartspaceUpdate smartspaceProto$SmartspaceUpdate = new MessageNano() { // from class: com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate
                    public SmartspaceCard[] card;

                    /* loaded from: classes.dex */
                    public static final class SmartspaceCard extends MessageNano {
                        private static volatile SmartspaceCard[] _emptyArray;
                        public int cardId;
                        public int cardPriority;
                        public int cardType;
                        public Message duringEvent;
                        public long eventDurationMillis;
                        public long eventTimeMillis;
                        public ExpiryCriteria expiryCriteria;
                        public Image icon;
                        public boolean isSensitive;
                        public boolean isWorkProfile;
                        public Message postEvent;
                        public Message preEvent;
                        public boolean shouldDiscard;
                        public TapAction tapAction;
                        public long updateTimeMillis;

                        /* loaded from: classes.dex */
                        public static final class Message extends MessageNano {
                            public FormattedText subtitle;
                            public FormattedText title;

                            /* loaded from: classes.dex */
                            public static final class FormattedText extends MessageNano {
                                public FormatParam[] formatParam;
                                public String text;
                                public int truncateLocation;

                                /* loaded from: classes.dex */
                                public static final class FormatParam extends MessageNano {
                                    private static volatile FormatParam[] _emptyArray;
                                    public int formatParamArgs;
                                    public String text;
                                    public int truncateLocation;
                                    public boolean updateTimeLocally;

                                    public static FormatParam[] emptyArray() {
                                        if (_emptyArray == null) {
                                            synchronized (InternalNano.LAZY_INIT_LOCK) {
                                                if (_emptyArray == null) {
                                                    _emptyArray = new FormatParam[0];
                                                }
                                            }
                                        }
                                        return _emptyArray;
                                    }

                                    public FormatParam() {
                                        clear();
                                    }

                                    public FormatParam clear() {
                                        this.text = "";
                                        this.truncateLocation = 0;
                                        this.formatParamArgs = 0;
                                        this.updateTimeLocally = false;
                                        this.cachedSize = -1;
                                        return this;
                                    }

                                    @Override // com.google.protobuf.nano.MessageNano
                                    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                                        if (!this.text.equals("")) {
                                            codedOutputByteBufferNano.writeString(1, this.text);
                                        }
                                        int i = this.truncateLocation;
                                        if (i != 0) {
                                            codedOutputByteBufferNano.writeInt32(2, i);
                                        }
                                        int i2 = this.formatParamArgs;
                                        if (i2 != 0) {
                                            codedOutputByteBufferNano.writeInt32(3, i2);
                                        }
                                        boolean z = this.updateTimeLocally;
                                        if (z) {
                                            codedOutputByteBufferNano.writeBool(4, z);
                                        }
                                        super.writeTo(codedOutputByteBufferNano);
                                    }

                                    @Override // com.google.protobuf.nano.MessageNano
                                    protected int computeSerializedSize() {
                                        int computeSerializedSize = super.computeSerializedSize();
                                        if (!this.text.equals("")) {
                                            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.text);
                                        }
                                        int i = this.truncateLocation;
                                        if (i != 0) {
                                            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i);
                                        }
                                        int i2 = this.formatParamArgs;
                                        if (i2 != 0) {
                                            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i2);
                                        }
                                        boolean z = this.updateTimeLocally;
                                        return z ? computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(4, z) : computeSerializedSize;
                                    }

                                    @Override // com.google.protobuf.nano.MessageNano
                                    public FormatParam mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                                        while (true) {
                                            int readTag = codedInputByteBufferNano.readTag();
                                            if (readTag == 0) {
                                                return this;
                                            }
                                            if (readTag == 10) {
                                                this.text = codedInputByteBufferNano.readString();
                                            } else if (readTag == 16) {
                                                int readInt32 = codedInputByteBufferNano.readInt32();
                                                if (readInt32 == 0 || readInt32 == 1 || readInt32 == 2 || readInt32 == 3) {
                                                    this.truncateLocation = readInt32;
                                                }
                                            } else if (readTag == 24) {
                                                int readInt322 = codedInputByteBufferNano.readInt32();
                                                if (readInt322 == 0 || readInt322 == 1 || readInt322 == 2 || readInt322 == 3) {
                                                    this.formatParamArgs = readInt322;
                                                }
                                            } else if (readTag == 32) {
                                                this.updateTimeLocally = codedInputByteBufferNano.readBool();
                                            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                                return this;
                                            }
                                        }
                                    }
                                }

                                public FormattedText() {
                                    clear();
                                }

                                public FormattedText clear() {
                                    this.text = "";
                                    this.truncateLocation = 0;
                                    this.formatParam = FormatParam.emptyArray();
                                    this.cachedSize = -1;
                                    return this;
                                }

                                @Override // com.google.protobuf.nano.MessageNano
                                public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                                    if (!this.text.equals("")) {
                                        codedOutputByteBufferNano.writeString(1, this.text);
                                    }
                                    int i = this.truncateLocation;
                                    if (i != 0) {
                                        codedOutputByteBufferNano.writeInt32(2, i);
                                    }
                                    FormatParam[] formatParamArr = this.formatParam;
                                    if (formatParamArr != null && formatParamArr.length > 0) {
                                        int i2 = 0;
                                        while (true) {
                                            FormatParam[] formatParamArr2 = this.formatParam;
                                            if (i2 >= formatParamArr2.length) {
                                                break;
                                            }
                                            FormatParam formatParam = formatParamArr2[i2];
                                            if (formatParam != null) {
                                                codedOutputByteBufferNano.writeMessage(3, formatParam);
                                            }
                                            i2++;
                                        }
                                    }
                                    super.writeTo(codedOutputByteBufferNano);
                                }

                                @Override // com.google.protobuf.nano.MessageNano
                                protected int computeSerializedSize() {
                                    int computeSerializedSize = super.computeSerializedSize();
                                    if (!this.text.equals("")) {
                                        computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.text);
                                    }
                                    int i = this.truncateLocation;
                                    if (i != 0) {
                                        computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i);
                                    }
                                    FormatParam[] formatParamArr = this.formatParam;
                                    if (formatParamArr != null && formatParamArr.length > 0) {
                                        int i2 = 0;
                                        while (true) {
                                            FormatParam[] formatParamArr2 = this.formatParam;
                                            if (i2 >= formatParamArr2.length) {
                                                break;
                                            }
                                            FormatParam formatParam = formatParamArr2[i2];
                                            if (formatParam != null) {
                                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(3, formatParam);
                                            }
                                            i2++;
                                        }
                                    }
                                    return computeSerializedSize;
                                }

                                @Override // com.google.protobuf.nano.MessageNano
                                public FormattedText mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                                    while (true) {
                                        int readTag = codedInputByteBufferNano.readTag();
                                        if (readTag == 0) {
                                            return this;
                                        }
                                        if (readTag == 10) {
                                            this.text = codedInputByteBufferNano.readString();
                                        } else if (readTag == 16) {
                                            int readInt32 = codedInputByteBufferNano.readInt32();
                                            if (readInt32 == 0 || readInt32 == 1 || readInt32 == 2 || readInt32 == 3) {
                                                this.truncateLocation = readInt32;
                                            }
                                        } else if (readTag == 26) {
                                            int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 26);
                                            FormatParam[] formatParamArr = this.formatParam;
                                            int length = formatParamArr == null ? 0 : formatParamArr.length;
                                            int i = repeatedFieldArrayLength + length;
                                            FormatParam[] formatParamArr2 = new FormatParam[i];
                                            if (length != 0) {
                                                System.arraycopy(formatParamArr, 0, formatParamArr2, 0, length);
                                            }
                                            while (length < i - 1) {
                                                formatParamArr2[length] = new FormatParam();
                                                codedInputByteBufferNano.readMessage(formatParamArr2[length]);
                                                codedInputByteBufferNano.readTag();
                                                length++;
                                            }
                                            formatParamArr2[length] = new FormatParam();
                                            codedInputByteBufferNano.readMessage(formatParamArr2[length]);
                                            this.formatParam = formatParamArr2;
                                        } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                            return this;
                                        }
                                    }
                                }
                            }

                            public Message() {
                                clear();
                            }

                            public Message clear() {
                                this.title = null;
                                this.subtitle = null;
                                this.cachedSize = -1;
                                return this;
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                                FormattedText formattedText = this.title;
                                if (formattedText != null) {
                                    codedOutputByteBufferNano.writeMessage(1, formattedText);
                                }
                                FormattedText formattedText2 = this.subtitle;
                                if (formattedText2 != null) {
                                    codedOutputByteBufferNano.writeMessage(2, formattedText2);
                                }
                                super.writeTo(codedOutputByteBufferNano);
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            protected int computeSerializedSize() {
                                int computeSerializedSize = super.computeSerializedSize();
                                FormattedText formattedText = this.title;
                                if (formattedText != null) {
                                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, formattedText);
                                }
                                FormattedText formattedText2 = this.subtitle;
                                return formattedText2 != null ? computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, formattedText2) : computeSerializedSize;
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            public Message mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                                while (true) {
                                    int readTag = codedInputByteBufferNano.readTag();
                                    if (readTag == 0) {
                                        return this;
                                    }
                                    if (readTag == 10) {
                                        if (this.title == null) {
                                            this.title = new FormattedText();
                                        }
                                        codedInputByteBufferNano.readMessage(this.title);
                                    } else if (readTag == 18) {
                                        if (this.subtitle == null) {
                                            this.subtitle = new FormattedText();
                                        }
                                        codedInputByteBufferNano.readMessage(this.subtitle);
                                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                        return this;
                                    }
                                }
                            }
                        }

                        /* loaded from: classes.dex */
                        public static final class Image extends MessageNano {
                            public String gsaResourceName;
                            public String key;
                            public String uri;

                            public Image() {
                                clear();
                            }

                            public Image clear() {
                                this.key = "";
                                this.gsaResourceName = "";
                                this.uri = "";
                                this.cachedSize = -1;
                                return this;
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                                if (!this.key.equals("")) {
                                    codedOutputByteBufferNano.writeString(1, this.key);
                                }
                                if (!this.gsaResourceName.equals("")) {
                                    codedOutputByteBufferNano.writeString(2, this.gsaResourceName);
                                }
                                if (!this.uri.equals("")) {
                                    codedOutputByteBufferNano.writeString(3, this.uri);
                                }
                                super.writeTo(codedOutputByteBufferNano);
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            protected int computeSerializedSize() {
                                int computeSerializedSize = super.computeSerializedSize();
                                if (!this.key.equals("")) {
                                    computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.key);
                                }
                                if (!this.gsaResourceName.equals("")) {
                                    computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.gsaResourceName);
                                }
                                return !this.uri.equals("") ? computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(3, this.uri) : computeSerializedSize;
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            public Image mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                                while (true) {
                                    int readTag = codedInputByteBufferNano.readTag();
                                    if (readTag == 0) {
                                        return this;
                                    }
                                    if (readTag == 10) {
                                        this.key = codedInputByteBufferNano.readString();
                                    } else if (readTag == 18) {
                                        this.gsaResourceName = codedInputByteBufferNano.readString();
                                    } else if (readTag == 26) {
                                        this.uri = codedInputByteBufferNano.readString();
                                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                        return this;
                                    }
                                }
                            }
                        }

                        /* loaded from: classes.dex */
                        public static final class TapAction extends MessageNano {
                            public int actionType;
                            public String intent;

                            public TapAction() {
                                clear();
                            }

                            public TapAction clear() {
                                this.actionType = 0;
                                this.intent = "";
                                this.cachedSize = -1;
                                return this;
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                                int i = this.actionType;
                                if (i != 0) {
                                    codedOutputByteBufferNano.writeInt32(1, i);
                                }
                                if (!this.intent.equals("")) {
                                    codedOutputByteBufferNano.writeString(2, this.intent);
                                }
                                super.writeTo(codedOutputByteBufferNano);
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            protected int computeSerializedSize() {
                                int computeSerializedSize = super.computeSerializedSize();
                                int i = this.actionType;
                                if (i != 0) {
                                    computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
                                }
                                return !this.intent.equals("") ? computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.intent) : computeSerializedSize;
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            public TapAction mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                                while (true) {
                                    int readTag = codedInputByteBufferNano.readTag();
                                    if (readTag == 0) {
                                        return this;
                                    }
                                    if (readTag == 8) {
                                        int readInt32 = codedInputByteBufferNano.readInt32();
                                        if (readInt32 == 0 || readInt32 == 1 || readInt32 == 2) {
                                            this.actionType = readInt32;
                                        }
                                    } else if (readTag == 18) {
                                        this.intent = codedInputByteBufferNano.readString();
                                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                        return this;
                                    }
                                }
                            }
                        }

                        /* loaded from: classes.dex */
                        public static final class ExpiryCriteria extends MessageNano {
                            public long expirationTimeMillis;
                            public int maxImpressions;

                            public ExpiryCriteria() {
                                clear();
                            }

                            public ExpiryCriteria clear() {
                                this.expirationTimeMillis = 0;
                                this.maxImpressions = 0;
                                this.cachedSize = -1;
                                return this;
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                                long j = this.expirationTimeMillis;
                                if (j != 0) {
                                    codedOutputByteBufferNano.writeInt64(1, j);
                                }
                                int i = this.maxImpressions;
                                if (i != 0) {
                                    codedOutputByteBufferNano.writeInt32(2, i);
                                }
                                super.writeTo(codedOutputByteBufferNano);
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            protected int computeSerializedSize() {
                                int computeSerializedSize = super.computeSerializedSize();
                                long j = this.expirationTimeMillis;
                                if (j != 0) {
                                    computeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(1, j);
                                }
                                int i = this.maxImpressions;
                                return i != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, i) : computeSerializedSize;
                            }

                            @Override // com.google.protobuf.nano.MessageNano
                            public ExpiryCriteria mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                                while (true) {
                                    int readTag = codedInputByteBufferNano.readTag();
                                    if (readTag == 0) {
                                        return this;
                                    }
                                    if (readTag == 8) {
                                        this.expirationTimeMillis = codedInputByteBufferNano.readInt64();
                                    } else if (readTag == 16) {
                                        this.maxImpressions = codedInputByteBufferNano.readInt32();
                                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                        return this;
                                    }
                                }
                            }
                        }

                        public static SmartspaceCard[] emptyArray() {
                            if (_emptyArray == null) {
                                synchronized (InternalNano.LAZY_INIT_LOCK) {
                                    if (_emptyArray == null) {
                                        _emptyArray = new SmartspaceCard[0];
                                    }
                                }
                            }
                            return _emptyArray;
                        }

                        public SmartspaceCard() {
                            clear();
                        }

                        public SmartspaceCard clear() {
                            this.shouldDiscard = false;
                            this.cardPriority = 0;
                            this.cardId = 0;
                            this.preEvent = null;
                            this.duringEvent = null;
                            this.postEvent = null;
                            this.icon = null;
                            this.cardType = 0;
                            this.tapAction = null;
                            this.updateTimeMillis = 0;
                            this.eventTimeMillis = 0;
                            this.eventDurationMillis = 0;
                            this.expiryCriteria = null;
                            this.isSensitive = false;
                            this.isWorkProfile = false;
                            this.cachedSize = -1;
                            return this;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            boolean z = this.shouldDiscard;
                            if (z) {
                                codedOutputByteBufferNano.writeBool(1, z);
                            }
                            int i = this.cardId;
                            if (i != 0) {
                                codedOutputByteBufferNano.writeInt32(2, i);
                            }
                            Message message = this.preEvent;
                            if (message != null) {
                                codedOutputByteBufferNano.writeMessage(3, message);
                            }
                            Message message2 = this.duringEvent;
                            if (message2 != null) {
                                codedOutputByteBufferNano.writeMessage(4, message2);
                            }
                            Message message3 = this.postEvent;
                            if (message3 != null) {
                                codedOutputByteBufferNano.writeMessage(5, message3);
                            }
                            Image image = this.icon;
                            if (image != null) {
                                codedOutputByteBufferNano.writeMessage(6, image);
                            }
                            int i2 = this.cardType;
                            if (i2 != 0) {
                                codedOutputByteBufferNano.writeInt32(7, i2);
                            }
                            TapAction tapAction = this.tapAction;
                            if (tapAction != null) {
                                codedOutputByteBufferNano.writeMessage(8, tapAction);
                            }
                            long j = this.updateTimeMillis;
                            if (j != 0) {
                                codedOutputByteBufferNano.writeInt64(9, j);
                            }
                            long j2 = this.eventTimeMillis;
                            if (j2 != 0) {
                                codedOutputByteBufferNano.writeInt64(10, j2);
                            }
                            long j3 = this.eventDurationMillis;
                            if (j3 != 0) {
                                codedOutputByteBufferNano.writeInt64(11, j3);
                            }
                            ExpiryCriteria expiryCriteria = this.expiryCriteria;
                            if (expiryCriteria != null) {
                                codedOutputByteBufferNano.writeMessage(12, expiryCriteria);
                            }
                            int i3 = this.cardPriority;
                            if (i3 != 0) {
                                codedOutputByteBufferNano.writeInt32(13, i3);
                            }
                            boolean z2 = this.isSensitive;
                            if (z2) {
                                codedOutputByteBufferNano.writeBool(17, z2);
                            }
                            boolean z3 = this.isWorkProfile;
                            if (z3) {
                                codedOutputByteBufferNano.writeBool(18, z3);
                            }
                            super.writeTo(codedOutputByteBufferNano);
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        protected int computeSerializedSize() {
                            int computeSerializedSize = super.computeSerializedSize();
                            boolean z = this.shouldDiscard;
                            if (z) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(1, z);
                            }
                            int i = this.cardId;
                            if (i != 0) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i);
                            }
                            Message message = this.preEvent;
                            if (message != null) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(3, message);
                            }
                            Message message2 = this.duringEvent;
                            if (message2 != null) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(4, message2);
                            }
                            Message message3 = this.postEvent;
                            if (message3 != null) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(5, message3);
                            }
                            Image image = this.icon;
                            if (image != null) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(6, image);
                            }
                            int i2 = this.cardType;
                            if (i2 != 0) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, i2);
                            }
                            TapAction tapAction = this.tapAction;
                            if (tapAction != null) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(8, tapAction);
                            }
                            long j = this.updateTimeMillis;
                            if (j != 0) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(9, j);
                            }
                            long j2 = this.eventTimeMillis;
                            if (j2 != 0) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(10, j2);
                            }
                            long j3 = this.eventDurationMillis;
                            if (j3 != 0) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(11, j3);
                            }
                            ExpiryCriteria expiryCriteria = this.expiryCriteria;
                            if (expiryCriteria != null) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(12, expiryCriteria);
                            }
                            int i3 = this.cardPriority;
                            if (i3 != 0) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i3);
                            }
                            boolean z2 = this.isSensitive;
                            if (z2) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(17, z2);
                            }
                            boolean z3 = this.isWorkProfile;
                            return z3 ? computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(18, z3) : computeSerializedSize;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public SmartspaceCard mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                            while (true) {
                                int readTag = codedInputByteBufferNano.readTag();
                                switch (readTag) {
                                    case 0:
                                        return this;
                                    case 8:
                                        this.shouldDiscard = codedInputByteBufferNano.readBool();
                                        break;
                                    case 16:
                                        this.cardId = codedInputByteBufferNano.readInt32();
                                        break;
                                    case 26:
                                        if (this.preEvent == null) {
                                            this.preEvent = new Message();
                                        }
                                        codedInputByteBufferNano.readMessage(this.preEvent);
                                        break;
                                    case 34:
                                        if (this.duringEvent == null) {
                                            this.duringEvent = new Message();
                                        }
                                        codedInputByteBufferNano.readMessage(this.duringEvent);
                                        break;
                                    case 42:
                                        if (this.postEvent == null) {
                                            this.postEvent = new Message();
                                        }
                                        codedInputByteBufferNano.readMessage(this.postEvent);
                                        break;
                                    case 50:
                                        if (this.icon == null) {
                                            this.icon = new Image();
                                        }
                                        codedInputByteBufferNano.readMessage(this.icon);
                                        break;
                                    case 56:
                                        int readInt32 = codedInputByteBufferNano.readInt32();
                                        switch (readInt32) {
                                            case 0:
                                            case 1:
                                            case 2:
                                            case 3:
                                            case 4:
                                            case 5:
                                            case 6:
                                                this.cardType = readInt32;
                                                continue;
                                        }
                                    case 66:
                                        if (this.tapAction == null) {
                                            this.tapAction = new TapAction();
                                        }
                                        codedInputByteBufferNano.readMessage(this.tapAction);
                                        break;
                                    case 72:
                                        this.updateTimeMillis = codedInputByteBufferNano.readInt64();
                                        break;
                                    case 80:
                                        this.eventTimeMillis = codedInputByteBufferNano.readInt64();
                                        break;
                                    case 88:
                                        this.eventDurationMillis = codedInputByteBufferNano.readInt64();
                                        break;
                                    case R$styleable.Constraint_layout_goneMarginBottom:
                                        if (this.expiryCriteria == null) {
                                            this.expiryCriteria = new ExpiryCriteria();
                                        }
                                        codedInputByteBufferNano.readMessage(this.expiryCriteria);
                                        break;
                                    case R$styleable.Constraint_motionStagger:
                                        int readInt322 = codedInputByteBufferNano.readInt32();
                                        if (readInt322 != 0 && readInt322 != 1 && readInt322 != 2) {
                                            break;
                                        } else {
                                            this.cardPriority = readInt322;
                                            break;
                                        }
                                        break;
                                    case 136:
                                        this.isSensitive = codedInputByteBufferNano.readBool();
                                        break;
                                    case 144:
                                        this.isWorkProfile = codedInputByteBufferNano.readBool();
                                        break;
                                    default:
                                        if (WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                            break;
                                        } else {
                                            return this;
                                        }
                                }
                            }
                        }
                    }

                    {
                        clear();
                    }

                    public SmartspaceProto$SmartspaceUpdate clear() {
                        this.card = SmartspaceCard.emptyArray();
                        this.cachedSize = -1;
                        return this;
                    }

                    @Override // com.google.protobuf.nano.MessageNano
                    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                        SmartspaceCard[] smartspaceCardArr = this.card;
                        if (smartspaceCardArr != null && smartspaceCardArr.length > 0) {
                            int i = 0;
                            while (true) {
                                SmartspaceCard[] smartspaceCardArr2 = this.card;
                                if (i >= smartspaceCardArr2.length) {
                                    break;
                                }
                                SmartspaceCard smartspaceCard = smartspaceCardArr2[i];
                                if (smartspaceCard != null) {
                                    codedOutputByteBufferNano.writeMessage(1, smartspaceCard);
                                }
                                i++;
                            }
                        }
                        super.writeTo(codedOutputByteBufferNano);
                    }

                    @Override // com.google.protobuf.nano.MessageNano
                    protected int computeSerializedSize() {
                        int computeSerializedSize = super.computeSerializedSize();
                        SmartspaceCard[] smartspaceCardArr = this.card;
                        if (smartspaceCardArr != null && smartspaceCardArr.length > 0) {
                            int i = 0;
                            while (true) {
                                SmartspaceCard[] smartspaceCardArr2 = this.card;
                                if (i >= smartspaceCardArr2.length) {
                                    break;
                                }
                                SmartspaceCard smartspaceCard = smartspaceCardArr2[i];
                                if (smartspaceCard != null) {
                                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, smartspaceCard);
                                }
                                i++;
                            }
                        }
                        return computeSerializedSize;
                    }

                    @Override // com.google.protobuf.nano.MessageNano
                    public SmartspaceProto$SmartspaceUpdate mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                        while (true) {
                            int readTag = codedInputByteBufferNano.readTag();
                            if (readTag == 0) {
                                return this;
                            }
                            if (readTag == 10) {
                                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                                SmartspaceCard[] smartspaceCardArr = this.card;
                                int length = smartspaceCardArr == null ? 0 : smartspaceCardArr.length;
                                int i = repeatedFieldArrayLength + length;
                                SmartspaceCard[] smartspaceCardArr2 = new SmartspaceCard[i];
                                if (length != 0) {
                                    System.arraycopy(smartspaceCardArr, 0, smartspaceCardArr2, 0, length);
                                }
                                while (length < i - 1) {
                                    smartspaceCardArr2[length] = new SmartspaceCard();
                                    codedInputByteBufferNano.readMessage(smartspaceCardArr2[length]);
                                    codedInputByteBufferNano.readTag();
                                    length++;
                                }
                                smartspaceCardArr2[length] = new SmartspaceCard();
                                codedInputByteBufferNano.readMessage(smartspaceCardArr2[length]);
                                this.card = smartspaceCardArr2;
                            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                return this;
                            }
                        }
                    }
                };
                try {
                    MessageNano.mergeFrom(smartspaceProto$SmartspaceUpdate, byteArrayExtra);
                    SmartspaceProto$SmartspaceUpdate.SmartspaceCard[] smartspaceCardArr = smartspaceProto$SmartspaceUpdate.card;
                    for (SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard : smartspaceCardArr) {
                        int i = smartspaceCard.cardPriority;
                        boolean z = i == 1;
                        boolean z2 = i == 2;
                        if (!z && !z2) {
                            Log.w("SmartSpaceReceiver", "unrecognized card priority: " + smartspaceCard.cardPriority);
                        }
                        notify(smartspaceCard, context, intent, z);
                    }
                } catch (InvalidProtocolBufferNanoException e) {
                    Log.e("SmartSpaceReceiver", "proto", e);
                }
            } else {
                Log.e("SmartSpaceReceiver", "receiving update with no proto: " + intent.getExtras());
            }
        } else if (!intent.getBooleanExtra("rebroadcast", false)) {
            intent.putExtra("rebroadcast", true);
            intent.putExtra("uid", myUserId);
            context.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    private void notify(SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard, Context context, Intent intent, boolean z) {
        PackageInfo packageInfo;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.google.android.googlequicksearchbox", 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("SmartSpaceReceiver", "Cannot find GSA", e);
            packageInfo = null;
        }
        this.mController.onNewCard(new NewCardInfo(smartspaceCard, intent, z, currentTimeMillis, packageInfo));
    }
}
