package br.com.sicredi.sync.tool.balancer.balancer;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Guarda informacoes da execucao de update de status de um determinado
 * equipamento.
 *
 * @param <Id>
 *            Identificador do processo
 *
 * @author dalpiaz
 */
public class ProcessStatus<Id> implements Externalizable {

    private static final long serialVersionUID = 7182169778471535680L;

    private Id processId;
    private Date lastRequestTimestamp;
    private ProcessExecutionState currentState;

    public ProcessStatus() {
        // Used for serialization
    }

    public ProcessStatus(Id processId) {
        this.processId = processId;
        this.lastRequestTimestamp = Date.from(Instant.EPOCH);
        this.currentState = ProcessExecutionState.STOPPED;
    }

    public Id getProcessId() {
        return processId;
    }

    public Date getLastRequestTimestamp() {
        return lastRequestTimestamp;
    }

    public void setLastRequestTimestamp(Date lastRequestTimestamp) {
        this.lastRequestTimestamp = lastRequestTimestamp;
    }

    public ProcessExecutionState getCurrentProcessState() {
        return this.currentState;
    }

    public void setProcessState(ProcessExecutionState newState) {
        this.currentState = newState;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(processId);
        objectOutput.writeLong(lastRequestTimestamp.getTime());
        objectOutput.writeUTF(currentState.name());
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.processId = (Id) objectInput.readObject();
        this.lastRequestTimestamp = new Date(objectInput.readLong());
        this.currentState = ProcessExecutionState.valueOf(objectInput.readUTF());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ProcessStatus) {
            ProcessStatus<Id> that = (ProcessStatus<Id>) obj;

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
                .append("LastRequestDate", this.lastRequestTimestamp) //$NON-NLS-1$
                .append("CurrentState", this.currentState) //$NON-NLS-1$
                .toString();
    }
}
