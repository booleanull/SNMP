import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SNMPManager {

    private Snmp snmp = null;
    private String address = null;

    public final OID[] oids = new OID[] { new OID(".1.3.6.1.2.1.1.4.0"), new OID(".1.3.6.1.2.1.1.5.0"), new OID(".1.3.6.1.2.1.1.6.0"), new OID(".1.3.6.1.2.1.1.1.0") };

    public SNMPManager(String add)
    {
        address = add;
        try {
            start();
        } catch (Exception exception) {

        }
    }

    private void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public String getAsString(OID[] oid) throws IOException {
        ResponseEvent event = get(oid);
        if (event.getResponse() == null) return null;
        else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < oids.length; i++) {
                builder.append(event.getResponse().get(i).toValueString());
                builder.append(";");
            }
            return builder.toString();
        }
    }

    public ResponseEvent get(OID oids[]) throws IOException {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if(event != null) {
            return event;
        }
        throw new RuntimeException("GET timed out");
    }

    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("booleanull123456"));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }
}
