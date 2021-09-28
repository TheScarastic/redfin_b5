package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.android.systemui.shared.R;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ListMenuItemView extends LinearLayout implements MenuView.ItemView, AbsListView.SelectionBoundsAdjuster {
    public Drawable mBackground;
    public CheckBox mCheckBox;
    public LinearLayout mContent;
    public boolean mForceShowIcon;
    public ImageView mGroupDivider;
    public boolean mHasListDivider;
    public ImageView mIconView;
    public LayoutInflater mInflater;
    public MenuItemImpl mItemData;
    public boolean mPreserveIconSpacing;
    public RadioButton mRadioButton;
    public TextView mShortcutView;
    public Drawable mSubMenuArrow;
    public ImageView mSubMenuArrowView;
    public int mTextAppearance;
    public Context mTextAppearanceContext;
    public TextView mTitleView;

    public ListMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.listMenuViewStyle);
    }

    @Override // android.widget.AbsListView.SelectionBoundsAdjuster
    public void adjustListItemSelectionBounds(Rect rect) {
        ImageView imageView = this.mGroupDivider;
        if (imageView != null && imageView.getVisibility() == 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mGroupDivider.getLayoutParams();
            rect.top = this.mGroupDivider.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin + rect.top;
        }
    }

    public final LayoutInflater getInflater() {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(getContext());
        }
        return this.mInflater;
    }

    @Override // androidx.appcompat.view.menu.MenuView.ItemView
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    @Override // androidx.appcompat.view.menu.MenuView.ItemView
    public void initialize(MenuItemImpl menuItemImpl, int i) {
        ImageView imageView;
        String str;
        CompoundButton compoundButton;
        CompoundButton compoundButton2;
        this.mItemData = menuItemImpl;
        int i2 = 0;
        setVisibility(menuItemImpl.isVisible() ? 0 : 8);
        CharSequence charSequence = menuItemImpl.mTitle;
        if (charSequence != null) {
            this.mTitleView.setText(charSequence);
            if (this.mTitleView.getVisibility() != 0) {
                this.mTitleView.setVisibility(0);
            }
        } else if (this.mTitleView.getVisibility() != 8) {
            this.mTitleView.setVisibility(8);
        }
        boolean isCheckable = menuItemImpl.isCheckable();
        if (!(!isCheckable && this.mRadioButton == null && this.mCheckBox == null)) {
            if ((this.mItemData.mFlags & 4) != 0) {
                if (this.mRadioButton == null) {
                    RadioButton radioButton = (RadioButton) getInflater().inflate(R.layout.abc_list_menu_item_radio, (ViewGroup) this, false);
                    this.mRadioButton = radioButton;
                    LinearLayout linearLayout = this.mContent;
                    if (linearLayout != null) {
                        linearLayout.addView(radioButton, -1);
                    } else {
                        addView(radioButton, -1);
                    }
                }
                compoundButton2 = this.mRadioButton;
                compoundButton = this.mCheckBox;
            } else {
                if (this.mCheckBox == null) {
                    CheckBox checkBox = (CheckBox) getInflater().inflate(R.layout.abc_list_menu_item_checkbox, (ViewGroup) this, false);
                    this.mCheckBox = checkBox;
                    LinearLayout linearLayout2 = this.mContent;
                    if (linearLayout2 != null) {
                        linearLayout2.addView(checkBox, -1);
                    } else {
                        addView(checkBox, -1);
                    }
                }
                compoundButton2 = this.mCheckBox;
                compoundButton = this.mRadioButton;
            }
            if (isCheckable) {
                compoundButton2.setChecked(this.mItemData.isChecked());
                if (compoundButton2.getVisibility() != 0) {
                    compoundButton2.setVisibility(0);
                }
                if (!(compoundButton == null || compoundButton.getVisibility() == 8)) {
                    compoundButton.setVisibility(8);
                }
            } else {
                CheckBox checkBox2 = this.mCheckBox;
                if (checkBox2 != null) {
                    checkBox2.setVisibility(8);
                }
                RadioButton radioButton2 = this.mRadioButton;
                if (radioButton2 != null) {
                    radioButton2.setVisibility(8);
                }
            }
        }
        boolean shouldShowShortcut = menuItemImpl.shouldShowShortcut();
        menuItemImpl.getShortcut();
        int i3 = (!shouldShowShortcut || !this.mItemData.shouldShowShortcut()) ? 8 : 0;
        if (i3 == 0) {
            TextView textView = this.mShortcutView;
            MenuItemImpl menuItemImpl2 = this.mItemData;
            char shortcut = menuItemImpl2.getShortcut();
            if (shortcut == 0) {
                str = "";
            } else {
                Resources resources = menuItemImpl2.mMenu.mContext.getResources();
                StringBuilder sb = new StringBuilder();
                if (ViewConfiguration.get(menuItemImpl2.mMenu.mContext).hasPermanentMenuKey()) {
                    sb.append(resources.getString(R.string.abc_prepend_shortcut_label));
                }
                int i4 = menuItemImpl2.mMenu.isQwertyMode() ? menuItemImpl2.mShortcutAlphabeticModifiers : menuItemImpl2.mShortcutNumericModifiers;
                MenuItemImpl.appendModifier(sb, i4, QuickStepContract.SYSUI_STATE_ONE_HANDED_ACTIVE, resources.getString(R.string.abc_menu_meta_shortcut_label));
                MenuItemImpl.appendModifier(sb, i4, QuickStepContract.SYSUI_STATE_TRACING_ENABLED, resources.getString(R.string.abc_menu_ctrl_shortcut_label));
                MenuItemImpl.appendModifier(sb, i4, 2, resources.getString(R.string.abc_menu_alt_shortcut_label));
                MenuItemImpl.appendModifier(sb, i4, 1, resources.getString(R.string.abc_menu_shift_shortcut_label));
                MenuItemImpl.appendModifier(sb, i4, 4, resources.getString(R.string.abc_menu_sym_shortcut_label));
                MenuItemImpl.appendModifier(sb, i4, 8, resources.getString(R.string.abc_menu_function_shortcut_label));
                if (shortcut == '\b') {
                    sb.append(resources.getString(R.string.abc_menu_delete_shortcut_label));
                } else if (shortcut == '\n') {
                    sb.append(resources.getString(R.string.abc_menu_enter_shortcut_label));
                } else if (shortcut != ' ') {
                    sb.append(shortcut);
                } else {
                    sb.append(resources.getString(R.string.abc_menu_space_shortcut_label));
                }
                str = sb.toString();
            }
            textView.setText(str);
        }
        if (this.mShortcutView.getVisibility() != i3) {
            this.mShortcutView.setVisibility(i3);
        }
        Drawable icon = menuItemImpl.getIcon();
        Objects.requireNonNull(this.mItemData.mMenu);
        boolean z = this.mForceShowIcon;
        if ((z || this.mPreserveIconSpacing) && !((imageView = this.mIconView) == null && icon == null && !this.mPreserveIconSpacing)) {
            if (imageView == null) {
                ImageView imageView2 = (ImageView) getInflater().inflate(R.layout.abc_list_menu_item_icon, (ViewGroup) this, false);
                this.mIconView = imageView2;
                LinearLayout linearLayout3 = this.mContent;
                if (linearLayout3 != null) {
                    linearLayout3.addView(imageView2, 0);
                } else {
                    addView(imageView2, 0);
                }
            }
            if (icon != null || this.mPreserveIconSpacing) {
                ImageView imageView3 = this.mIconView;
                if (!z) {
                    icon = null;
                }
                imageView3.setImageDrawable(icon);
                if (this.mIconView.getVisibility() != 0) {
                    this.mIconView.setVisibility(0);
                }
            } else {
                this.mIconView.setVisibility(8);
            }
        }
        setEnabled(menuItemImpl.isEnabled());
        boolean hasSubMenu = menuItemImpl.hasSubMenu();
        ImageView imageView4 = this.mSubMenuArrowView;
        if (imageView4 != null) {
            if (!hasSubMenu) {
                i2 = 8;
            }
            imageView4.setVisibility(i2);
        }
        setContentDescription(menuItemImpl.mContentDescription);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        Drawable drawable = this.mBackground;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        setBackground(drawable);
        TextView textView = (TextView) findViewById(R.id.title);
        this.mTitleView = textView;
        int i = this.mTextAppearance;
        if (i != -1) {
            textView.setTextAppearance(this.mTextAppearanceContext, i);
        }
        this.mShortcutView = (TextView) findViewById(R.id.shortcut);
        ImageView imageView = (ImageView) findViewById(R.id.submenuarrow);
        this.mSubMenuArrowView = imageView;
        if (imageView != null) {
            imageView.setImageDrawable(this.mSubMenuArrow);
        }
        this.mGroupDivider = (ImageView) findViewById(R.id.group_divider);
        this.mContent = (LinearLayout) findViewById(R.id.content);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (this.mIconView != null && this.mPreserveIconSpacing) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
            int i3 = layoutParams.height;
            if (i3 > 0 && layoutParams2.width <= 0) {
                layoutParams2.width = i3;
            }
        }
        super.onMeasure(i, i2);
    }

    public ListMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(getContext(), attributeSet, R$styleable.MenuView, i, 0);
        this.mBackground = obtainStyledAttributes.getDrawable(5);
        this.mTextAppearance = obtainStyledAttributes.getResourceId(1, -1);
        this.mPreserveIconSpacing = obtainStyledAttributes.getBoolean(7, false);
        this.mTextAppearanceContext = context;
        this.mSubMenuArrow = obtainStyledAttributes.getDrawable(8);
        TypedArray obtainStyledAttributes2 = context.getTheme().obtainStyledAttributes(null, new int[]{16843049}, R.attr.dropDownListViewStyle, 0);
        this.mHasListDivider = obtainStyledAttributes2.hasValue(0);
        obtainStyledAttributes.mWrapped.recycle();
        obtainStyledAttributes2.recycle();
    }
}
