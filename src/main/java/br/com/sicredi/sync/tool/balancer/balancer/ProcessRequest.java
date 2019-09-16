package br.com.sicredi.sync.tool.balancer.balancer;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Guarda informacoes de um pedido de execucao de update de status de um
 * determinado equipamento.
 *
 * @param <Id>
 *            Identificador do processo
 *
 * @author dalpiaz
 */
public class ProcessRequest<Id> implements Externalizable {

    private Id processId;
    private Date timestamp;

    public ProcessRequest() {
        // Used for serialization
    }

    public ProcessRequest(Id processId, Date timestamp) {
        this.processId = processId;
        this.timestamp = timestamp;
    }

    public Id getProcessId() {
        return processId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(processId);
        objectOutput.writeLong(timestamp.getTime());
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.processId = (Id) objectInput.readObject();
        this.timestamp = new Date(objectInput.readLong());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ProcessRequest) {
            ProcessRequest<Id> that = (ProcessRequest<Id>) obj;

            return (this.processId.equals(that.processId));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.processId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("ProcessId", this.processId) //$NON-NLS-1$
                .append("LastRequestDate", this.timestamp) //$NON-NLS-1$
                .toString();
    }
}
