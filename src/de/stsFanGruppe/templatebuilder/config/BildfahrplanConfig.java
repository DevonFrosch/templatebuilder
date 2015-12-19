package de.stsFanGruppe.templatebuilder.config;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import de.stsFanGruppe.tools.JSONBuilder;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanConfig
{
	private Map<Object, ChangeCallback> callbacks = new HashMap<>();
	private int callbackCounter = 0;
	
	protected int marginRight = 20;
	protected int marginLeft = 20;
	protected int marginTop = 0;
	protected int marginBottom = 20;
	
	//Einstellung für den Bildfahrplanspaltenheader
	int lineHeight = 10;
	int offsetX = 10;
	int offsetY = 5;
	int textMarginTop = 5;
	int textMarginBottom = 5;
	int zeilenAnzahl = 2;
	
	//Einstellung für den Bildfahrplanzeilenheader
	int zeilenHeaderBreite = 32;
	int zeitIntervall = 10;
	
	//Farbeinstellungen
	Color zeitFarbe = Color.RED;
	Color fahrtenFarbe = Color.BLACK;
	Color betriebsstelleFarbe = Color.BLUE;
	
	protected int hoeheProStunde = 400;
	protected double minZeit = 360;
	protected double maxZeit = 1260;
	protected boolean autoSize = true;
	protected int schachtelung = 24;
	protected boolean zeichneZeiten = true; 
	
	protected int zeigeZugnamen = 2;
	protected boolean zeigeZugnamenKommentare = true;
	

	
	public BildfahrplanConfig(double minZeit, double maxZeit)
	{
		this.setZeiten(minZeit, maxZeit);
	}
	public BildfahrplanConfig()
	{
		this.enableAutoSize();
	}
	
	public double getMinZeit()
	{
		return minZeit;
	}
	public double getMaxZeit()
	{
		return maxZeit;
	}
	public void setZeiten(double min, double max)
	{
		if(min < 0 || max < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein.");
		}
		this.minZeit = min;
		this.maxZeit = max;
		this.autoSize = false;
		notifyChange();
	}
	public boolean needsAutoSize()
	{
		return autoSize;
	}
	public void enableAutoSize()
	{
		this.autoSize = true;
	}
	public boolean isZeichneZeiten() {
		return zeichneZeiten;
	}
	public void setZeichneZeiten(boolean zeichneZeiten) {
		this.zeichneZeiten = zeichneZeiten;
	}
	public int getHoeheProStunde()
	{
		return hoeheProStunde;
	}
	public void setHoeheProStunde(int hoeheProStunde)
	{
		if(hoeheProStunde < 0)
		{
			throw new IllegalArgumentException("Höhe muss größer gleich 0 sein.");
		}
		this.hoeheProStunde = hoeheProStunde;
		notifyChange();
	}
	public int getMarginTop()
	{
		return this.marginTop;
	}
	public int getMarginLeft()
	{
		return this.marginLeft;
	}
	public int getMarginRight()
	{
		return this.marginRight;
	}
	public int getMarginBottom()
	{
		return this.marginBottom;
	}
	public int getZeigeZugnamen()
	{
		return zeigeZugnamen;
	}
	public void setZeigeZugnamen(int zeigeZugnamen)
	{
		this.zeigeZugnamen = zeigeZugnamen;
	}
	public int getZeilenHeaderBreite() {
		return zeilenHeaderBreite;
	}
	public int getZeitIntervall() {
		return zeitIntervall;
	}
	public void setZeitIntervall(int zeitIntervall) {
		this.zeitIntervall = zeitIntervall;
	}
	public void setZeilenHeaderBreite(int zeilenHeaderBreite) {
		this.zeilenHeaderBreite = zeilenHeaderBreite;
	}
	public Color getZeitFarbe() {
		return zeitFarbe;
	}
	public void setZeitFarbe(Color zeitFarbe) {
		this.zeitFarbe = zeitFarbe;
	}
	public Color getFahrtenFarbe() {
		return fahrtenFarbe;
	}
	public void setFahrtenFarbe(Color fahrtenFarbe) {
		this.fahrtenFarbe = fahrtenFarbe;
	}
	public Color getBetriebsstelleFarbe() {
		return betriebsstelleFarbe;
	}
	public void setBetriebsstelleFarbe(Color betriebsstelleFarbe) {
		this.betriebsstelleFarbe = betriebsstelleFarbe;
	}
	public void setZeigeZugnamen(String zeigeZugnamen)
	{
		switch(zeigeZugnamen)
		{
			case "nie":
				this.zeigeZugnamen = 0;
				break;
			case "immer":
				this.zeigeZugnamen = 1;
				break;
			case "auto":
			default:
				this.zeigeZugnamen = 2;
		}
	}
	public void setZeigeZugnamenKommentare(boolean zeigeZugnamenKommentare)
	{
		this.zeigeZugnamenKommentare = zeigeZugnamenKommentare;
	}
	public boolean getZeigeZugnamenKommentare()
	{
		return zeigeZugnamenKommentare;
	}
	public int getSchachtelung()
	{
		return schachtelung;
	}
	public void setSchachtelung(int schachtelung)
	{
		this.schachtelung = schachtelung;
	}
	public int getLineHeight() {
		return lineHeight;
	}
	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}
	public int getOffsetX() {
		return offsetX;
	}
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	public int getOffsetY() {
		return offsetY;
	}
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	public int getTextMarginTop() {
		return textMarginTop;
	}
	public void setTextMarginTop(int textMarginTop) {
		this.textMarginTop = textMarginTop;
	}
	public int getTextMarginBottom() {
		return textMarginBottom;
	}
	public void setTextMarginBottom(int textMarginBottom) {
		this.textMarginBottom = textMarginBottom;
	}
	public int getZeilenAnzahl() {
		return zeilenAnzahl;
	}
	public void setZeilenAnzahl(int zeilenAnzahl) {
		this.zeilenAnzahl = zeilenAnzahl;
	}
	public int getPanelHeight()
	{
		return (int) ((maxZeit - minZeit) / 60 * hoeheProStunde) + marginTop + marginBottom;
	}
	public int zeichnenBreite(JComponent p)
	{
		assert p != null;
		return p.getWidth() - marginLeft - marginRight;
	}
	public int zeichnenHoehe(JComponent p)
	{
		assert p != null;
		return p.getHeight() - marginTop - marginBottom;
	}
	public int spaltenHeaderHoehe(int textHeight){
		return textMarginTop + (textHeight + offsetY) * zeilenAnzahl + textMarginBottom;  
	}
	
	public Object registerChangeHandler(ChangeCallback callback)
	{
		NullTester.test(callback);
		Object handlerID = Integer.valueOf(callbackCounter++);
		callbacks.put(handlerID, callback);
		return handlerID;
	}
	public boolean unregisterChangeHandler(Object handlerID)
	{
		NullTester.test(handlerID);
		return callbacks.remove(handlerID) != null;
	}
	protected void notifyChange()
	{
		callbacks.forEach((k, v) -> v.call());
	}
	
	public void parseJSON(String json)
	{
		// TODO JSONParser einbauen
	}
	
	public String toJSON()
	{
		JSONBuilder json = new JSONBuilder();
		json.beginObject();
		json.add("marginRight", marginRight);
		json.add("marginLeft", marginLeft);
		json.add("marginTop", marginTop);
		json.add("marginBottom", marginBottom);
		json.add("hoeheProStunde", hoeheProStunde);
		json.add("minZeit", minZeit);
		json.add("maxZeit", maxZeit);
		json.add("autoSize", autoSize);
		json.add("schachtelung", schachtelung);
		json.endObject();
		return json.toString();
	}
	public String toString()
	{
		return "{margin: r="+marginRight+", l="+marginLeft+", t="+marginTop+", b="+marginBottom+"}";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		String newLine = "\n"+indent+"  ";
		return "<bildfahrplanConfig>"
				+newLine+"<hoeheProStunde value=\""+hoeheProStunde+"\" />"
				+newLine+"<zeitBereich min="+minZeit+" max=\""+maxZeit+"\" />"
				+"\n"+indent+"</bildfahrplanConfig>";
	}
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanConfig: "+text);
	}
	
	public interface ChangeCallback
	{
		public void call();
	}
}
