# Trust  
Document to maintain all actions that influence an agent's trust and competence. And, conversely, how an agent's trust and competence influences the actions of the robot. Actions are labeled `MSG` if they can be inferred from a message. Otherwise we need to dig into some if statement.

Trust consists of two factors: willingness and competence. Both are bounded between `[-1, 1]`. In my implementation, I treat `< 0.2` as `LOW`, and `> -0.2` as `HIGH` (not quite `0` to allow some room for fluctuations in the factors. We can easily tweak the values later). This should lead to the following convergence:

- Strong human: `HIGH` competence, `HIGH` willingness.
- Lying, normal human: `~0` competence, `LOW` willingness.
- Lazy, weak human: `LOW` competence, `~0` willingness. 

I also added confidence, which is bounded between `[0, 1]`. The agent starts out with low confidence, `0` or `0.1`, which gets multiplied with the competence and willingness values of the human. Given the above bounds for willingness and competence, this should mean the human is treated more favourably regardless of its characterstics, when the robot is just starting out.

Unlike trust values, confidence changes multiplicatively, i.e. increases by `30%`. I feel that this more accurately models how confidence works: you gradually build up confidence over time. Conversely, if you are very confident in someone, i.e. confidence at `1`, and they do something unexpected, this is significant and a `30%` decrease in confidence represents that significant change more accurately. 

## Competence
#### Actions that affect competence

- [x] Critically injured victims can only be carried by both human and RescueBot together. So requests for this should *not* affect competence. 

- [x] `MSG`: If a human collects a mildly injured victim, they are not weak. Thus, **increase competence**. However, they may be lying, so only do this if their willingness exceeds the `LOW` threshold.
    - Possible extension: In the statement where the robot proceeds to-research all rooms because the game is not complete, we can check which victims the human lied about rescuing. Then, reduce willingness by `*2` per victim, of what it was increased by originally.

- [x] `MSG`: If a human asks for help to carry a mildly injured victim, they could be weak. Thus, **reduce competence**. (Let's discard this?)-> However, they may be lying, so only do this if their willingness exceeds the `LOW` threshold. 
- [ ] If the robot actually comes to help, the human MUST be weak, so can **reduce competence** even more. 
    (Even though we cannot explicitly check for human characteristics, I assume we are allowed to change trust based on the robot behaviour, which checks for this in the baseline implementation.)

- [ ] `MSG`: If a human asks for help to remove the small rock, they could be weak. Thus, **reduce competence**. 
    - Again, if the robot actually comes to help, the human must be weak, so can **reduce competence** even more.

- [x] `MSG`: If human has already searched the same area before, **reduce competence** as he is not capable of remembering the areas he searched before.

Possible extensions for the midly injured victims and the small rocks could be to check for distance. If we assume that Strong and Normal humans only ask for help if the robot is nearby, we can reduce competence only if the human is far away.

#### How perceived competence affects robot actions
- A competent human may be able to remove small rocks/mildly injured victims by themselves. This saves time if the robot is not nearby. So, if trust (and competence) is high enough, robot may leave these objects for the human to rescue.

## Willingness 
#### Actions that affect willingness

- [x] if `'not present in'`: Robot discovered that victim is not present, reduce willingness.
- `NOTE`: I don't think this is possible with this code structure!! If human asks for help, but on getting there the object is already removed/not there, reduce willingness. Also, If the tree is not there, reduce willingness.
- [x] if the human asks for help for removing a big rock, but doesn't show up after a timeout, reduce willingness. 

- [x] If the human asks for help in removing a tree, increase willingness, as this action can only be performed by the robot. 
- `TODO`: If the tree is not there, reduce willingness. `NOTE`: Not really possible with the current code structure.
- [x] if the human sends a message about searching a room, finding a victim, asking for help to remove, removing an obstacle, increase willingness by a small amount, as it shows that the human is willing to play the game.
- [x] For 'search', if the human searches different places on top of each other without finding a victim/removing an obstacle in between, the human is probably lying, thus reduce willingness.
- [x] When the victim is found as human told, increase willingness
- [x] If human lied about the location of the victim, or rescued the victim without telling the agent, reduce willingness
- [x] When a victim is carried and dropped together, increase willingness as it means that a victim is saved together.
- [x] If the human does not respond in time, reduce willingness

#### How perceived willingness affects robot actions

- [x] On finding a tree: (Independent)

    - `TIMEOUT`: The robot waits `300` game ticks. If the human does not reply within this time, it defaults to `Remove`. Additionally, the robot skips the wait if willingness is `LOW`.

    - `Remove`: Increase willingness for replying in time.

    - `Continue`: This option is only accepted if willingness is `HIGH`. As this action can only be performed by the robot (and skipping it is therefore disadvantageous), willingness is reduced.

- [x] On finding a big rock: (Hard interdependency) Robot needs human to remove it. 
    - 'Remove Together': The human may lie about coming to help.  introduce an idling timeout for this action.
                         *If the human does not show up, reduce willingness.*
    - 'Continue': there is nothing we can do. Robot needs the human to remove the big rock. So simply move on and try again later.
                         *This should be the default option after an idle timeout, as the lazy human may take a while to respond. Could reduce willingness.*

- [x] On finding a critically injured victim: (Hard interdependency) Robot needs human to carry. 
    - 'Rescue': The human may lie about coming to help. If willingness is low, introduce an idling timeout for this action.
                *If the human does not show up, reduce willingness.*
    - 'Continue': There is nothing we can do. Robot needs the human to carry a critical victim. So, simply move on and try again later. This should be the default option after an idle timeout, as the lazy human may take a while to respond.
                  *Could reduce willingness for the same reasoning as above.*

- [x!] On finding a mildly injured victim: (Soft interdependency)
    - 'Continue': if willingness is low, default to 'Rescue alone'. This is because the lazy human may take a while to respond.
    - 'Rescue alone': if willingness is low, select this after a timeout. 
    - 'Rescue Together': If willingness is low, default to 'Rescue alone'. This is because the lying human may not show up.
                         *`STILL MISSING!` Otherwise, have an idling timeout. If the human does not show up, reduce willingness and default to rescue alone.*

- [x] On finding a small rock: (Soft interdependency, same as above)
    - 'Continue': If willingness is low, default to 'Remove alone'.
    - 'Remove alone': If willingness is low, select this after a timeout. 
    - 'Remove together': If willingness is low, default to 'Remove alone'.
                         *Otherwise, have an idling timeout. If the human does not show up, reduce willingness and default to 'Remove alone'.*


# Potential Improvements 
- While confidence in trust is low, the robot can follow the human to see whether their messages correspond to their actions, and update willingness based on that. 


# Notes


when the human says they have found something
1. they're lying 
    -> can't say they picked it up
2. truth
    -> can say they picked it up

because if you say you picked it up first, 
the robot will always assume it's the truth.
