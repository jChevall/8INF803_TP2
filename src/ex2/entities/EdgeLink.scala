package entities

class EdgeLink (var relation: String = "") extends Serializable {

    override def toString: String = "Relation : " + relation
}
