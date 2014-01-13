package com.eroreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class SettingsView extends TabActivity {

	private final static int AUTHORIZE_DIALOG = 0;
	private final static int CHANGE_PASS = 1;

	private EditText pass;
	private EditText pass_first;
	private EditText pass_second;
	private TextView incor;
	private Button ok;
	private Button canc;
	BaseAdapter adapter;
	private Display display;
	public static boolean FROM_TEXT = false;
	public static Context reader_context;
	public boolean really_exit;

	public void refresh() {
		adapter.notifyDataSetChanged();
		if (Utils.settings.change_atr == Settings.NAME_FULLSCREEN || Utils.settings.change_atr == Settings.NAME_ORIENTATION_LOCK){
			if (Utils.settings.FULLSCREEN)
				 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			else
				this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			if (Utils.settings.ORIENTATION_LOCK == 1)
				Utils.ORIENTATION = false;
			if (Utils.settings.ORIENTATION_LOCK == 2)
				Utils.ORIENTATION = true;
			if (Utils.settings.ORIENTATION_LOCK == 0){
			    int orientation = display.getRotation();
				Utils.ORIENTATION = ((orientation == Surface.ROTATION_0) | (orientation == Surface.ROTATION_180));
			}
			if (Utils.ORIENTATION){
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		} 
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if (Utils.settings.BRIGHTNESS != 0)
			lp.screenBrightness = ((float)Settings.VALUE_BRIGHTNESS[Utils.settings.BRIGHTNESS] / 100);
		getWindow().setAttributes(lp);  
	}

	@Override
	protected void onStop() {
		super.onStop(); 
		if (!FROM_TEXT)
			((MainMenu) Utils.main_context).refresh();
		SharedPreferences settings = getSharedPreferences(Utils.PREF, 0);
		SharedPreferences.Editor editor = settings.edit();
		Utils.settings.writeSettings(editor);
		if (FROM_TEXT && really_exit){
			FROM_TEXT = false;
			if (Utils.settings.criticalChangesPerformed(mem)){
				((ReaderActivity) reader_context).recreate();
			}
		}
	}
	
	public String mem;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Settings.applyScreenSettings(this);
		display = getWindowManager().getDefaultDisplay();
		OrientationEventListener orientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
			
			@Override
			public void onOrientationChanged(int orientation) {
				if (Utils.settings.ORIENTATION_LOCK != 0)
					return;
				if (orientation % 90 != 0)
					return;
				boolean f = Utils.ORIENTATION;
				orientation = display.getRotation();
				Utils.ORIENTATION = ((orientation == Surface.ROTATION_0) | (orientation == Surface.ROTATION_180));
				if (f != Utils.ORIENTATION) {
					refresh();
				}
			}
		};
        orientationListener.enable();
		
		setContentView(R.layout.settings);
		Utils.context = SettingsView.this;
		
		mem = Utils.settings.memento();
		really_exit = true;
		
		TabHost tabHost = getTabHost();
		TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
		firstTabSpec.setIndicator("Общие", getResources().getDrawable(R.drawable.browse)).setContent(
				new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						ListView list = new ListView(SettingsView.this);
						adapter = new BrowseAdapter(SettingsView.this,
								new String[5]);
						list.setAdapter(adapter); // 5 - number of atributes.
													// change if needed
						list.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								switch (position) {
								case BrowseAdapter.SHOW_ITEMS_PER_PAGE:
									Utils.settings.title = "Размер выдачи";
									Utils.settings.change_atr = Settings.NAME_SHOW_ITEMS_PER_PAGE;
									Utils.settings.selected = Utils.settings.SHOW_ITEMS_PER_PAGE;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case BrowseAdapter.PREVIEW_LENGTH:
									Utils.settings.title = "Длина цитаты";
									Utils.settings.change_atr = Settings.NAME_PREVIEW_LENGTH;
									Utils.settings.selected = Utils.settings.PREVIEW_LENGTH;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case BrowseAdapter.ORIENTATION_LOCK:
									Utils.settings.title = "Ориентация экрана";
									Utils.settings.change_atr = Settings.NAME_ORIENTATION_LOCK;
									Utils.settings.selected = Utils.settings.ORIENTATION_LOCK;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case BrowseAdapter.BRIGHTNESS:
									Utils.settings.title = "Яркость экрана";
									Utils.settings.change_atr = Settings.NAME_BRIGHTNESS;
									Utils.settings.selected = Utils.settings.BRIGHTNESS;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case BrowseAdapter.FULLSCREEN:
									Utils.settings.change_atr = Settings.NAME_FULLSCREEN;
									Utils.settings.FULLSCREEN = !Utils.settings.FULLSCREEN;
									refresh();
									break;
								default:
									break;// uhum
								}
							}
						});
						return list;
					}
				});
		tabHost.addTab(firstTabSpec);

		TabSpec secondTabSpec = tabHost.newTabSpec("tid2");
		secondTabSpec.setIndicator("Доступ", getResources().getDrawable(R.drawable.security)).setContent(
				new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						ListView list = new ListView(SettingsView.this);
						adapter = new SecurityAdapter(SettingsView.this,
								new String[2]);
						list.setAdapter(adapter); // 3 - number of atributes.
													// change if needed
						list.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								switch (position) {
								case SecurityAdapter.USE_PASSWORD:
									if (!Utils.settings.PASSWORD_SET)
										showDialog(CHANGE_PASS);
									Utils.settings.PASSWORD_SET = !Utils.settings.PASSWORD_SET;
									refresh();
									break;
							/*	case SecurityAdapter.EXIT_WITHOUT_PROMT:
									Utils.settings.EXIT_WITHOUT_PROMT = !Utils.settings.EXIT_WITHOUT_PROMT;
									refresh();
									break;*/
								case SecurityAdapter.SAVE_HISTORY:
									Utils.settings.SAVE_HISTORY = !Utils.settings.SAVE_HISTORY;
									refresh();
									break;
								default:
									break;// uhum
								}
							}
						});
						return list;
					}
				});
		tabHost.addTab(secondTabSpec);

		TabSpec thirdTabSpec = tabHost.newTabSpec("tid3");
		thirdTabSpec.setIndicator("Чтение", getResources().getDrawable(R.drawable.reader)).setContent(
				new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						ListView list = new ListView(SettingsView.this);
						adapter = new ReaderAdapter(SettingsView.this,
								new String[12]);
						list.setAdapter(adapter); // 3 - number of atributes.
													// change if needed
						list.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								switch (position) {
								case ReaderAdapter.FONT:
									Utils.settings.title = "Шрифт";
									Utils.settings.change_atr = Settings.NAME_FONT;
									Utils.settings.selected = Utils.settings.FONT;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case ReaderAdapter.FONT_SIZE:
									Utils.settings.title = "Размер шрифта";
									Utils.settings.change_atr = Settings.NAME_FONT_SIZE;
									Utils.settings.selected = Utils.settings.FONT_SIZE;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case ReaderAdapter.BOLD:
									Utils.settings.BOLD = !Utils.settings.BOLD;
									refresh();
									break;
								case ReaderAdapter.SHOW_STATUSBAR:
									Utils.settings.SHOW_STATUS_BAR = !Utils.settings.SHOW_STATUS_BAR;
									refresh();
									break;
								/*case ReaderAdapter.VIEW_MODE:
									Utils.settings.title = "Режим просмотра";
									Utils.settings.change_atr = Settings.NAME_VIEW_MODE;
									Utils.settings.selected = Utils.settings.VIEW_MODE;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;*/
							/*	case ReaderAdapter.LINE_SPACING:
									Utils.settings.title = "Межстрочное расстояние";
									Utils.settings.change_atr = Settings.NAME_LINE_SPACING;
									Utils.settings.selected = Utils.settings.LINE_SPACING;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;*/
								case ReaderAdapter.NIGHT_MODE:
									Utils.settings.NIGHT_MODE = !Utils.settings.NIGHT_MODE;
									refresh();
									break;
								case ReaderAdapter.FONT_COLOR:
									AmbilWarnaDialog dialog = new AmbilWarnaDialog(SettingsView.this, Utils.settings.getFontColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
										public void onOk(AmbilWarnaDialog dialog, int color) {
											Utils.settings.setFontColor(color);
											refresh();
										}
										public void onCancel(AmbilWarnaDialog dialog) {}
									});
									dialog.show();
									break;
								case ReaderAdapter.BACKGROUND_COLOR:
									dialog = new AmbilWarnaDialog(SettingsView.this, Utils.settings.getBackgroundColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
										public void onOk(AmbilWarnaDialog dialog, int color) {
											Utils.settings.setBackgroundColor(color);
											refresh();
										}
										public void onCancel(AmbilWarnaDialog dialog) {}
									});
									dialog.show();
									break;
								case ReaderAdapter.BACKGROUND_TEXTURE:
									Utils.settings.title = "Текстура фона";
									Utils.settings.change_atr = Settings.NAME_BACKGROUND_TEXTURE;
									Utils.settings.selected = Utils.settings.BACKGROUND_TEXTURE;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsTextureList.class));
									break;
								case ReaderAdapter.PADDING_LEFT:
									Utils.settings.title = "Отступ слева";
									Utils.settings.change_atr = Settings.NAME_PADDING_LEFT;
									Utils.settings.selected = Utils.settings.PADDING_LEFT;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case ReaderAdapter.PADDING_RIGHT:
									Utils.settings.title = "Отступ справа";
									Utils.settings.change_atr = Settings.NAME_PADDING_RIGHT;
									Utils.settings.selected = Utils.settings.PADDING_RIGHT;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case ReaderAdapter.PADDING_TOP:
									Utils.settings.title = "Отступ сверху";
									Utils.settings.change_atr = Settings.NAME_PADDING_TOP;
									Utils.settings.selected = Utils.settings.PADDING_TOP;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								case ReaderAdapter.PADDING_BOTTOM:
									Utils.settings.title = "Отступ снизу";
									Utils.settings.change_atr = Settings.NAME_PADDING_BOTTOM;
									Utils.settings.selected = Utils.settings.PADDING_BOTTOM;
									really_exit = false;
									startActivity(new Intent(SettingsView.this,
											SettingsSimpleList.class));
									break;
								default:
									break;// uhum
								}
							}
						});
						return list;
					}
				});
		tabHost.addTab(thirdTabSpec);
		
		tabHost.setCurrentTabByTag(FROM_TEXT ? "tid3" : "tid1");
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		LayoutInflater inflater;
		View layout;
		AlertDialog.Builder builder;

		switch (id) {
		case AUTHORIZE_DIALOG:
			inflater = getLayoutInflater();
			layout = inflater.inflate(R.layout.autoriz,
					(ViewGroup) findViewById(R.id.lin));

			pass = (EditText) layout.findViewById(R.id.pass_edit);
			incor = (TextView) layout.findViewById(R.id.incorrect);
			incor.setText("");

			pass.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void afterTextChanged(Editable s) {
					if (s.length() < 4) {
						ok.setEnabled(false);
						incor.setText("");
					} else
						ok.setEnabled(true);
				}
			});

			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setMessage("Введите пароль");

			ok = (Button) layout.findViewById(R.id.pass_ok);
			ok.setEnabled(false);
			canc = (Button) layout.findViewById(R.id.pass_canc);
			ok.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (pass.getText().toString()
							.equals(Integer.toString(Utils.settings.PASSWORD))) {
						pass.setText("");
						incor.setText("");
						dismissDialog(AUTHORIZE_DIALOG);
					}
				}
			});

			canc.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					pass.setText("");
					dismissDialog(AUTHORIZE_DIALOG);
				}
			});

			builder.setCancelable(false);
			return builder.create();
		case CHANGE_PASS:
			inflater = getLayoutInflater();
			layout = inflater.inflate(R.layout.change_pass,
					(ViewGroup) findViewById(R.id.lin1));

			pass_first = (EditText) layout.findViewById(R.id.pass_first);
			pass_second = (EditText) layout.findViewById(R.id.pass_second);
			pass_first.setText("");
			pass_second.setText("");
			incor = (TextView) layout.findViewById(R.id.incorrect1);
			incor.setText("");

			pass_second.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void afterTextChanged(Editable s) {
					if (pass_first.getText().toString()
							.equals(pass_second.getText().toString())) {
						if (pass_first.getText().toString().length() == 4) {
							incor.setText("");
							ok.setEnabled(true);
						} else {
							ok.setEnabled(false);
							incor.setText("Пароли слишком короткие.");
						}
					} else {
						ok.setEnabled(false);
						incor.setText("Пароли не совпадают.");
					}
				}
			});

			pass_first.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void afterTextChanged(Editable s) {
					if (pass_first.getText().toString()
							.equals(pass_second.getText().toString())) {
						if (pass_first.getText().toString().length() == 4) {
							incor.setText("");
							ok.setEnabled(true);
						} else {
							ok.setEnabled(false);
							incor.setText("Пароли слишком короткие.");
						}
					} else {
						ok.setEnabled(false);
						incor.setText("Пароли не совпадают.");
					}
				}
			});
			
			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setMessage("Смена пароля");

			ok = (Button) layout.findViewById(R.id.pass_ok1);
			ok.setEnabled(false);
			canc = (Button) layout.findViewById(R.id.pass_canc1);
			ok.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Utils.settings.PASSWORD = Integer.parseInt(pass_first
							.getText().toString());
					Utils.settings.PASSWORD_SET = true;
					pass_first.setText("");
					pass_second.setText("");
					ok.setEnabled(false);
					dismissDialog(CHANGE_PASS);
					refresh();
				}
			});
			canc.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Utils.settings.PASSWORD_SET = false;
					dismissDialog(CHANGE_PASS);
					refresh();
				}
			});

			builder.setCancelable(false);
			return builder.create();
		default:
			return null;
		}
	}
	
	private class BrowseAdapter extends BaseAdapter {
		private final Activity context;
		private final int itemCount;

		private static final int SHOW_ITEMS_PER_PAGE = 0;
		private static final int PREVIEW_LENGTH = 1;
		private static final int ORIENTATION_LOCK = 2;
		private static final int BRIGHTNESS = 3;
		private static final int FULLSCREEN = 4;

		public BrowseAdapter(Activity context, String[] name) {
			this.itemCount = name.length;
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View root = inflater.inflate(R.layout.settings_list_element, null,
					true);
			TextView name = (TextView) root.findViewById(R.id.setting_name);
			TextView value = (TextView) root.findViewById(R.id.setting_value);

			CheckBox box = (CheckBox) root.findViewById(R.id.box);
			box.setVisibility(View.GONE);

			ImageView color = (ImageView) root.findViewById(R.id.color_image);
			color.setVisibility(View.GONE);

			switch (position) {
			case SHOW_ITEMS_PER_PAGE:
				name.setText("Размер выдачи");
				value.setText(Integer.toString(Utils.settings
						.getAtr(Settings.NAME_SHOW_ITEMS_PER_PAGE)));
				break;
			case PREVIEW_LENGTH:
				name.setText("Длина цитаты");
				value.setText(Integer.toString(Utils.settings
						.getAtr(Settings.NAME_PREVIEW_LENGTH)));
				break;
			case ORIENTATION_LOCK:
				name.setText("Ориентация экрана");
				value.setText(Utils.settings
						.getAtrToStr(Settings.NAME_ORIENTATION_LOCK));
				break;
			case BRIGHTNESS:
				name.setText("Яркость экрана");
				value.setText(Utils.settings
						.getAtrToStr(Settings.NAME_BRIGHTNESS));
				break;
			case FULLSCREEN:
				name.setPadding(name.getPaddingLeft(),
						Math.round(10 * Utils.DENSITY), 0, 0);
				name.setText("На полный экран");
				box.setVisibility(View.VISIBLE);
				value.setVisibility(View.GONE);
				box.setChecked(Utils.settings.FULLSCREEN);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Utils.settings.FULLSCREEN = isChecked;
						Utils.settings.change_atr = Settings.NAME_FULLSCREEN;
						refresh();
					}
				});
			default:
				break;
			}

			return root;
		}

		public int getCount() {
			return itemCount;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

	}

	private class SecurityAdapter extends BaseAdapter {
		private final Activity context;
		private final int itemCount;

		private static final int USE_PASSWORD = 0;
	//	private static final int EXIT_WITHOUT_PROMT = 1;
		private static final int SAVE_HISTORY = 1;

		public SecurityAdapter(Activity context, String[] name) {
			this.itemCount = name.length;
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View root = inflater.inflate(R.layout.settings_list_element, null,
					true);
			TextView name = (TextView) root.findViewById(R.id.setting_name);
			TextView value = (TextView) root.findViewById(R.id.setting_value);
			CheckBox box = (CheckBox) root.findViewById(R.id.box);
			value.setVisibility(View.GONE);
			name.setPadding(name.getPaddingLeft(),
					Math.round(10 * Utils.DENSITY), 0, 0);

			ImageView color = (ImageView) root.findViewById(R.id.color_image);
			color.setVisibility(View.GONE);

			switch (position) {
			case USE_PASSWORD:
				name.setText("Использовать пароль");
				box.setChecked(Utils.settings.PASSWORD_SET);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked)
							showDialog(CHANGE_PASS);
						Utils.settings.PASSWORD_SET = isChecked;
					}
				});
				break;
		/*	case EXIT_WITHOUT_PROMT:
				name.setText("Выходить без предупреждения");
				box.setChecked(Utils.settings.EXIT_WITHOUT_PROMT);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Utils.settings.EXIT_WITHOUT_PROMT = isChecked;
					}
				});
				break;*/
			case SAVE_HISTORY:
				name.setText("Сохранять историю");
				box.setChecked(Utils.settings.SAVE_HISTORY);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Utils.settings.SAVE_HISTORY = isChecked;
					}
				});
				break;
			default:
				break;
			}

			return root;
		}

		public int getCount() {
			return itemCount;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

	}

	private class ReaderAdapter extends BaseAdapter {
		private final Activity context;
		private final int itemCount;

		private static final int FONT = 0;
		private static final int FONT_SIZE = 1;
		private static final int BOLD = 2;
		private static final int SHOW_STATUSBAR = 3;
		//private static final int VIEW_MODE = 4;
		//private static final int LINE_SPACING = 5;
		private static final int NIGHT_MODE = 4;
		private static final int FONT_COLOR = 5;
		private static final int BACKGROUND_COLOR = 6;
		private static final int BACKGROUND_TEXTURE = 7;
		private static final int PADDING_LEFT = 8;
		private static final int PADDING_RIGHT = 9;
		private static final int PADDING_TOP = 10;
		private static final int PADDING_BOTTOM = 11;

		// private static final int SHADOW = 14;

		public ReaderAdapter(Activity context, String[] name) {
			this.itemCount = name.length;
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View root = inflater.inflate(R.layout.settings_list_element, null,
					true);
			TextView name = (TextView) root.findViewById(R.id.setting_name);
			TextView value = (TextView) root.findViewById(R.id.setting_value);

			CheckBox box = (CheckBox) root.findViewById(R.id.box);
			box.setVisibility(View.GONE);

			ImageView color = (ImageView) root.findViewById(R.id.color_image);
			color.setVisibility(View.GONE);

			switch (position) {
			case FONT:
				name.setText("Шрифт");
				value.setText(Utils.settings.getAtrToStr(Settings.NAME_FONT));
				break;
			case FONT_SIZE:
				name.setText("Размер шрифта");
				value.setText(Integer.toString(Utils.settings
						.getAtr(Settings.NAME_FONT_SIZE)));
				break;
			case BOLD:
				name.setText("Жирность");
				name.setPadding(name.getPaddingLeft(),
						Math.round(10 * Utils.DENSITY), 0, 0);
				value.setVisibility(View.GONE);
				box.setVisibility(View.VISIBLE);
				box.setChecked(Utils.settings.BOLD);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Utils.settings.BOLD = isChecked;
					}
				});
				break;
			case SHOW_STATUSBAR:
				name.setText("Показывать статусбар");
				name.setPadding(name.getPaddingLeft(),
						Math.round(10 * Utils.DENSITY), 0, 0);
				value.setVisibility(View.GONE);
				box.setVisibility(View.VISIBLE);
				box.setChecked(Utils.settings.SHOW_STATUS_BAR);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Utils.settings.SHOW_STATUS_BAR = isChecked;
					}
				});
				break;
			/*case VIEW_MODE:
				name.setText("Режим просмотра");
				value.setText(Utils.settings
						.getAtrToStr(Settings.NAME_VIEW_MODE));
				break;
			case LINE_SPACING:
				name.setText("Межстрочное расстояние");
				value.setText(Integer.toString(Utils.settings
						.getAtr(Settings.NAME_LINE_SPACING)) + "%");
				break;*/
			case NIGHT_MODE:
				name.setText("Ночной режим");
				name.setPadding(name.getPaddingLeft(),
						Math.round(10 * Utils.DENSITY), 0, 0);
				value.setVisibility(View.GONE);
				box.setVisibility(View.VISIBLE);
				box.setChecked(Utils.settings.NIGHT_MODE);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Utils.settings.NIGHT_MODE = isChecked;
						refresh();
					}
				});
				break;
			case FONT_COLOR:
				name.setText("Цвет шрифта");
				name.setPadding(name.getPaddingLeft(),
						Math.round(10 * Utils.DENSITY), 0, 0);
				value.setVisibility(View.GONE);
				color.setVisibility(View.VISIBLE);
				int width = Math.round(30 * Utils.DENSITY);
				int height = Math.round(30 * Utils.DENSITY);
				root.setPadding(root.getPaddingLeft(), root.getPaddingTop(),
						root.getPaddingRight(), Math.round(10 * Utils.DENSITY));
				color.setImageBitmap(Bitmap.createBitmap(
						Utils.getColorArray(width, height,
								Utils.settings.getFontColor()), width, height,
						Bitmap.Config.ARGB_8888));
				break;
			case BACKGROUND_COLOR:
				name.setText("Цвет фона");
				name.setPadding(name.getPaddingLeft(),
						Math.round(10 * Utils.DENSITY), 0, 0);
				value.setVisibility(View.GONE);
				color.setVisibility(View.VISIBLE);
				width = Math.round(30 * Utils.DENSITY);
				height = Math.round(30 * Utils.DENSITY);
				root.setPadding(root.getPaddingLeft(), root.getPaddingTop(),
						root.getPaddingRight(), Math.round(10 * Utils.DENSITY));
				color.setImageBitmap(Bitmap.createBitmap(
						Utils.getColorArray(width, height,
								Utils.settings.getBackgroundColor()), width,
						height, Bitmap.Config.ARGB_8888));
				break;
			case BACKGROUND_TEXTURE:
				name.setText("Текстура фона");
				value.setText(Utils.settings
						.getAtrToStr(Settings.NAME_BACKGROUND_TEXTURE));
				break;
			case PADDING_LEFT:
				name.setText("Отступ слева");
				value.setText(Integer.toString(Utils.settings
						.getAtr(Settings.NAME_PADDING_LEFT)));
				break;
			case PADDING_RIGHT:
				name.setText("Отступ справа");
				value.setText(Integer.toString(Utils.settings
						.getAtr(Settings.NAME_PADDING_RIGHT)));
				break;
			case PADDING_TOP:
				name.setText("Отступ сверху");
				value.setText(Integer.toString(Utils.settings
						.getAtr(Settings.NAME_PADDING_TOP)));
				break;
			case PADDING_BOTTOM:
				name.setText("Отступ снизу");
				value.setText(Integer.toString(Utils.settings
						.getAtr(Settings.NAME_PADDING_BOTTOM)));
				break;
			default:
				break;
			}
			return root;
		}

		public int getCount() {
			return itemCount;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
	}
	
}