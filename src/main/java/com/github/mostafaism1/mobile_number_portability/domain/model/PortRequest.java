package com.github.mostafaism1.mobile_number_portability.domain.model;

import java.time.Instant;

public record PortRequest(Long id, MobileNumber mobileNumber, Operator donor, Operator recipient,
    Instant createdAt, State state) {

  public PortRequest transition(State transitionState) {
    if (state.canTransitionTo(transitionState))
      throw new IllegalRequestStateTransitionException(state, transitionState);

    return cloneWithState(state);
  }

  private PortRequest cloneWithState(State state) {
    return new PortRequest(this.id, this.mobileNumber, this.donor, this.recipient, this.createdAt,
        state);
  }

  public static enum State {
    PENDING {
      @Override
      public boolean canTransitionTo(State transitionState) {
        return !transitionState.equals(this);
      }
    },
    CANCELED {
      @Override
      public boolean canTransitionTo(State transitionState) {
        return false;
      }
    },
    REJECTED {
      @Override
      public boolean canTransitionTo(State transitionState) {
        return false;
      }
    },
    ACCEPTED {
      @Override
      public boolean canTransitionTo(State transitionState) {
        return false;
      }
    };

    public abstract boolean canTransitionTo(State transitionState);

  }

  public static class IllegalRequestStateTransitionException extends RuntimeException {

    public IllegalRequestStateTransitionException(State currentState, State transitionState) {
      super(String.format("Cannot change request state from [%s] to [%s].", currentState,
          transitionState));
    }

  }

}


