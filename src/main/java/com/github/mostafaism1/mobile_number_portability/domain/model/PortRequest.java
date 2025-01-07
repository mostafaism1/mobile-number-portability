package com.github.mostafaism1.mobile_number_portability.domain.model;

import java.time.Instant;

public record PortRequest(Long id, MobileNumber mobileNumber, Operator donor, Operator recipient,
    Instant createdAt, States state) {

  public PortRequest transition(States transitionState) {
    if (!state.canTransitionTo(transitionState))
      throw new IllegalRequestStateTransitionException(state, transitionState);

    return cloneWithState(transitionState);
  }

  private PortRequest cloneWithState(States state) {
    return new PortRequest(this.id, this.mobileNumber, this.donor, this.recipient, this.createdAt,
        state);
  }

  public static enum States {
    PENDING {
      @Override
      public boolean canTransitionTo(States transitionState) {
        return !transitionState.equals(this);
      }
    },
    CANCELED {
      @Override
      public boolean canTransitionTo(States transitionState) {
        return false;
      }
    },
    REJECTED {
      @Override
      public boolean canTransitionTo(States transitionState) {
        return false;
      }
    },
    ACCEPTED {
      @Override
      public boolean canTransitionTo(States transitionState) {
        return false;
      }
    };

    public abstract boolean canTransitionTo(States transitionState);

  }

  public static class IllegalRequestStateTransitionException extends RuntimeException {

    private IllegalRequestStateTransitionException(States currentState, States transitionState) {
      super(String.format("Cannot change request state from [%s] to [%s].", currentState,
          transitionState));
    }

  }

}


