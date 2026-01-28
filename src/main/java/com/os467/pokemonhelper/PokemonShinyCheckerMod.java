package com.os467.pokemonhelper;

import com.os467.constant.Constant;
import com.os467.pokemonhelper.translation.AbilityTranslation;
import com.os467.pokemonhelper.translation.EggMovesTranslation;
import com.os467.pokemonhelper.translation.PokemonNatureTranslation;
import com.os467.pokemonhelper.translation.ToCNTranslation;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class PokemonShinyCheckerMod implements ClientModInitializer {

	public static final String MOD_ID = Constant.MOD_ID;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ToCNTranslation natureTranslation = new PokemonNatureTranslation();
	public static final ToCNTranslation eggMovesTranslation = new EggMovesTranslation();
	public static final ToCNTranslation abilityTranslation = new AbilityTranslation();

	private static Class p;
	private static Method m;
	static {
		try {
			p = Class.forName("com.cobblemon.mod.common.api.pokemon.PokemonProperties");
			m = p.getDeclaredMethod("saveToJSON");
		} catch (ClassNotFoundException | NoSuchMethodException e) {

		}
	}

	@Override
	public void onInitializeClient() {

		// 注册事件监听器
		ItemTooltipCallback.EVENT.register(this::onItemTooltip);

	}




	private String getEggInfo(ItemStack stack){
		ComponentMap components = stack.getComponents();
		Set<ComponentType<?>> types = components.getTypes();
		for (ComponentType<?> type : types) {
			if ("cobbreeding:egg_info".equals(type.toString())){
				Object o = stack.getOrDefault(type, NbtComponent.DEFAULT);
				return o.toString();
			}

			/*if ("com.cobblemon.mod.common.api.pokemon.PokemonProperties".equals(o.getClass().getTypeName())){
				//反射继续获取
				try {
					JsonObject jsonObject = (JsonObject) m.invoke(o);
					nbt = jsonObject.toString();
					System.out.println(nbt);
				} catch (IllegalAccessException e) {

				} catch (InvocationTargetException e) {

				}
			}*/
		}
		return "";
	}


	private void onItemTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		// 检查物品是否包含pokemon_egg
		if (ItemIdChecker.contains(stack, "pokemon_egg")) {

			// 检查 NBT 标签
			String nbt = getEggInfo(stack);
			if (nbt == null){
				return;
			}


			// 检查物品的 NBT 标签
			//闪光
			if (nbt.contains("shiny=true")) {
				ShinyModifier.modify(lines);
			}


			String[] el = nbt.split("_iv=");
			String hp = el[1].split(" ")[0];
			String at = el[2].split(" ")[0];
			String de = el[3].split(" ")[0];
			String sa = el[4].split(" ")[0];
			String sd = el[5].split(" ")[0];
			String sp = el[6].split(" ")[0];
			String s = hp + "," + at + "," + de +"," + sa + "," +sd + "," + sp;

			addIVTips(lines,"§3速度:",sp);
			addIVTips(lines,"§e特防:",sd);
			addIVTips(lines,"§d特攻:",sa);
			addIVTips(lines,"§6防御:",de);
			addIVTips(lines,"§c攻击:",at);
			addIVTips(lines,"§a生命:",hp);

			//6V
			if (s.equals("31,31,31,31,31,31")){
				SixVModifier.modify(lines);
			}else {
				//5V

				//生命
				if (Pattern.compile("^\\d+,31,31,31,31,31$").matcher(s).matches()){
					boolean checked = lines.stream().anyMatch(line -> line.getString().contains("[5V]"));
					if (!checked){
						if (lines.size() > 1){
							lines.add(1,Text.of("§a[5V]生命"));
						} else {
							lines.add(Text.of("§a[5V]生命"));
						}
					}
				}

				//攻击
				if (Pattern.compile("^31,\\d+,31,31,31,31$").matcher(s).matches()){
					boolean checked = lines.stream().anyMatch(line -> line.getString().contains("[5V]"));
					if (!checked){
						if (lines.size() > 1){
							lines.add(1,Text.of("§c[5V]攻击"));
						} else {
							lines.add(Text.of("§c[5V]攻击"));
						}
					}
				}

				//防御
				if (Pattern.compile("^31,31,\\d+,31,31,31$").matcher(s).matches()){
					boolean checked = lines.stream().anyMatch(line -> line.getString().contains("[5V]"));
					if (!checked){
						if (lines.size() > 1){
							lines.add(1,Text.of("§6[5V]防御"));
						} else {
							lines.add(Text.of("§6[5V]防御"));
						}
					}
				}

				//特攻
				if (Pattern.compile("^31,31,31,\\d+,31,31$").matcher(s).matches()){
					boolean checked = lines.stream().anyMatch(line -> line.getString().contains("[5V]"));
					if (!checked){
						if (lines.size() > 1){
							lines.add(1,Text.of("§d[5V]特攻"));
						} else {
							lines.add(Text.of("§d[5V]特攻"));
						}
					}
				}

				//特防
				if (Pattern.compile("^31,31,31,31,\\d+,31$").matcher(s).matches()){
					boolean checked = lines.stream().anyMatch(line -> line.getString().contains("[5V]"));
					if (!checked){
						if (lines.size() > 1){
							lines.add(1,Text.of("§e[5V]特防"));
						} else {
							lines.add(Text.of("§e[5V]特防"));
						}
					}
				}

				//速度
				if (Pattern.compile("^31,31,31,31,31,\\d+$").matcher(s).matches()){
					boolean checked = lines.stream().anyMatch(line -> line.getString().contains("[5V]"));
					if (!checked){
						if (lines.size() > 1){
							lines.add(1,Text.of("§3[5V]速度"));
						} else {
							lines.add(Text.of("§3[5V]速度"));
						}
					}
				}

			}

			//性格
			NbtHandler nbtHandler = new NbtHandler(natureTranslation, lines, nbt);
			nbtHandler.handleInfo("nature=cobblemon:"," ","性格: ");

			//特性
			new NbtHandler(abilityTranslation,lines,nbt).handleInfo("ability="," ","特性:");

			//地区
			new NbtHandler(null,lines,nbt).handleInfo("form="," ","地区形态:");





		}
	}

	private void addIVTips(List<Text> lines, String flag, String iv) {
		boolean checked = lines.stream().anyMatch(line -> line.getString().contains(flag));
		if (!checked){
			if (lines.size() > 1){
				lines.add(1,Text.of(flag + iv));
			} else {
				lines.add(Text.of(flag + iv));
			}
		}
	}

	static class NbtHandler{
		private ToCNTranslation toCN;
		private List<Text> lines;
		private String nbt;

		public NbtHandler(ToCNTranslation toCN, List<Text> lines, String nbt) {
			this.toCN = toCN;
			this.lines = lines;
			this.nbt = nbt;
		}

		private void handleInfo(String start, String end, String flag) {
			//性格
			String s1 = nbt;
			s1 = s1.substring(s1.indexOf(start) + start.length());
			s1 = s1.substring(0, s1.indexOf(end));

			boolean checked = lines.stream().anyMatch(line -> line.getString().contains(flag));
			if (!checked){
				String info = s1;
				if (toCN != null){
					info = toCN.translateToChinese(s1);
				}
				if (lines.size() > 1){
					lines.add(1,Text.of(flag + info));
				} else {
					lines.add(Text.of(flag + info));
				}
			}
		}

	}


}