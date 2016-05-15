# delphi servlet make description file

PRODUCT=delphi
WARPATH=servlet/WEB-INF/classes
SERVAPI=artifact/servlet-api.jar

# contents of war file
MANIFEST=\
META-INF/context.xml \
WEB-INF/classes/oracle.class \
WEB-INF/classes/delphi/SHA1.class \
WEB-INF/lib/postgresql-jdbc.jar \
WEB-INF/lib/oracle-jdbc.jar \
WEB-INF/properties \
WEB-INF/web.xml \
images/dice.png \
images/home.png \
images/loading.gif \
images/backdrop.jpg \
images/services.jpg \
images/butterfly.png \
images/soccerball.png \
images/padlock.png \
images/system.png \
images/earth.png \
images/waves.jpg \
images/world.jpg \
script/style.css \
script/functions.js \
script/jquery-1.11.3.js \
script/overlay.css \
script/overlay.js \
script/sha1.js \
mainpage.jsp \
location.jsp \
profiles.jsp \
services.jsp \
index.html \
reload.jsp

# build dependencies
DEPS=\
servlet/META-INF/context.xml \
servlet/WEB-INF/classes/delphi/SHA1.class \
servlet/WEB-INF/classes/oracle.class \
servlet/WEB-INF/properties \
servlet/WEB-INF/web.xml \
servlet/script/style.css \
servlet/script/functions.js \
servlet/script/overlay.js \
servlet/script/sha1.js \
servlet/mainpage.jsp \
servlet/location.jsp \
servlet/profiles.jsp \
servlet/services.jsp \
servlet/index.html \
servlet/reload.jsp

# generated files
CLONES=\
servlet/META-INF/context.xml\
servlet/WEB-INF/web.xml

# generate rules
PROPS = servlet/WEB-INF/properties
DRIVER = $(shell grep dbms $(PROPS) | cut -d= -f2 | sed -e 's/^[ \t]*//')

$(WARPATH)/%.class: %.java
	javac -cp $(SERVAPI):$(WARPATH) -d $(WARPATH) $<

# make targets and deps
$(PRODUCT).war: $(DEPS)
	cd servlet; jar cf ../$(PRODUCT).war $(MANIFEST)

$(WARPATH)/oracle.class: oracle.java $(WARPATH)/delphi/SHA1.class

$(WARPATH)/delphi/SHA1.class: SHA1.java
	javac -d $(WARPATH) $<

servlet/META-INF/context.xml: $(PROPS)
	cp $@,$(DRIVER) $@

servlet/WEB-INF/web.xml: servlet/WEB-INF/web.xml,generic
	cp -p $< $@

clean:
	@echo -n "cleaning up.. "
	@rm -f $(PRODUCT).war $(WARPATH)/*.class $(WARPATH)/delphi/*.class $(CLONES)
	@echo "done."
